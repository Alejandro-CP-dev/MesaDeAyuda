package co.edu.sena.mesaayuda.repositorio;

import co.edu.sena.mesaayuda.modelo.Categoria;
import co.edu.sena.mesaayuda.modelo.Prioridad;
import co.edu.sena.mesaayuda.modelo.Ticket;
import co.edu.sena.mesaayuda.modelo.estado.EstadoTicketFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementacion JDBC de TicketRepository. Trae la Categoria y la Prioridad
 * completas con un JOIN (evita consultas N+1); los Comentarios NO se cargan
 * aqui para no traer datos de mas en los listados: ComentarioService los
 * pide aparte cuando se abre el detalle de un ticket.
 */
public class TicketRepositoryJdbc implements TicketRepository {

    private static final String SELECT_BASE =
            "SELECT t.Id, t.Titulo, t.Descripcion, t.Estado, t.FechaCreacion, t.FechaAsignacion, " +
            "       t.FechaResolucion, t.FechaCierre, t.FechaLimiteSla, t.SolicitanteId, t.AgenteId, " +
            "       t.CodigoCierre, " +
            "       c.Id AS CategoriaId, c.Nombre AS CategoriaNombre, " +
            "       p.Id AS PrioridadId, p.Nombre AS PrioridadNombre, p.HorasSla " +
            "FROM Ticket t " +
            "JOIN Categoria c ON c.Id = t.CategoriaId " +
            "JOIN Prioridad p ON p.Id = t.PrioridadId ";

    @Override
    public Ticket guardar(Ticket ticket) {
        String sql = "INSERT INTO Ticket (Titulo, Descripcion, Estado, FechaCreacion, FechaLimiteSla, " +
                "CategoriaId, PrioridadId, SolicitanteId, AgenteId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.obtener();
             PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setString(1, ticket.getTitulo());
            sentencia.setString(2, ticket.getDescripcion());
            sentencia.setString(3, ticket.getEstado().nombre());
            sentencia.setTimestamp(4, Timestamp.valueOf(ticket.getFechaCreacion()));
            sentencia.setTimestamp(5, aTimestamp(ticket.getFechaLimiteSla()));
            sentencia.setLong(6, ticket.getCategoria().getId());
            sentencia.setLong(7, ticket.getPrioridad().getId());
            sentencia.setLong(8, ticket.getSolicitanteId());
            if (ticket.getAgenteId() != null) {
                sentencia.setLong(9, ticket.getAgenteId());
            } else {
                sentencia.setNull(9, java.sql.Types.BIGINT);
            }
            sentencia.executeUpdate();

            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                claves.next();
                long id = claves.getLong(1);
                return buscarPorId(id).orElseThrow();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error guardando el ticket", e);
        }
    }

    @Override
    public void actualizar(Ticket ticket) {
        String sql = "UPDATE Ticket SET Estado = ?, FechaAsignacion = ?, FechaResolucion = ?, " +
                "FechaCierre = ?, FechaLimiteSla = ?, AgenteId = ?, CodigoCierre = ? WHERE Id = ?";
        try (Connection conexion = ConexionBD.obtener();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, ticket.getEstado().nombre());
            sentencia.setTimestamp(2, aTimestamp(ticket.getFechaAsignacion()));
            sentencia.setTimestamp(3, aTimestamp(ticket.getFechaResolucion()));
            sentencia.setTimestamp(4, aTimestamp(ticket.getFechaCierre()));
            sentencia.setTimestamp(5, aTimestamp(ticket.getFechaLimiteSla()));
            if (ticket.getAgenteId() != null) {
                sentencia.setLong(6, ticket.getAgenteId());
            } else {
                sentencia.setNull(6, java.sql.Types.BIGINT);
            }
            sentencia.setString(7, ticket.getCodigoCierre());
            sentencia.setLong(8, ticket.getId());
            sentencia.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando el ticket " + ticket.getId(), e);
        }
    }

    @Override
    public Optional<Ticket> buscarPorId(Long id) {
        String sql = SELECT_BASE + "WHERE t.Id = ?";
        try (Connection conexion = ConexionBD.obtener();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setLong(1, id);
            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next() ? Optional.of(mapear(resultado)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error consultando el ticket " + id, e);
        }
    }

    @Override
    public List<Ticket> listarTodos() {
        return listarConFiltro(SELECT_BASE + "ORDER BY t.FechaCreacion DESC", null);
    }

    @Override
    public List<Ticket> listarPorSolicitante(Long solicitanteId) {
        return listarConFiltro(SELECT_BASE + "WHERE t.SolicitanteId = ? ORDER BY t.FechaCreacion DESC", solicitanteId);
    }

    @Override
    public List<Ticket> listarPorAgente(Long agenteId) {
        return listarConFiltro(SELECT_BASE + "WHERE t.AgenteId = ? ORDER BY t.FechaCreacion DESC", agenteId);
    }

    @Override
    public List<Ticket> listarAbiertos() {
        String sql = SELECT_BASE + "WHERE t.Estado NOT IN ('CERRADO', 'CANCELADO')";
        return listarConFiltro(sql, null);
    }

    private List<Ticket> listarConFiltro(String sql, Long parametro) {
        List<Ticket> tickets = new ArrayList<>();
        try (Connection conexion = ConexionBD.obtener();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            if (parametro != null) {
                sentencia.setLong(1, parametro);
            }
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    tickets.add(mapear(resultado));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando tickets", e);
        }
        return tickets;
    }

    private Ticket mapear(ResultSet resultado) throws SQLException {
        Categoria categoria = new Categoria(resultado.getLong("CategoriaId"), resultado.getString("CategoriaNombre"));
        Prioridad prioridad = new Prioridad(resultado.getLong("PrioridadId"), resultado.getString("PrioridadNombre"),
                resultado.getInt("HorasSla"));

        return new Ticket(
                resultado.getLong("Id"),
                resultado.getString("Titulo"),
                resultado.getString("Descripcion"),
                EstadoTicketFactory.desdeNombre(resultado.getString("Estado")),
                aLocalDateTime(resultado.getTimestamp("FechaCreacion")),
                aLocalDateTime(resultado.getTimestamp("FechaAsignacion")),
                aLocalDateTime(resultado.getTimestamp("FechaResolucion")),
                aLocalDateTime(resultado.getTimestamp("FechaCierre")),
                aLocalDateTime(resultado.getTimestamp("FechaLimiteSla")),
                categoria,
                prioridad,
                resultado.getLong("SolicitanteId"),
                resultado.getObject("AgenteId") != null ? resultado.getLong("AgenteId") : null,
                resultado.getString("CodigoCierre"));
    }

    private Timestamp aTimestamp(LocalDateTime fecha) {
        return fecha != null ? Timestamp.valueOf(fecha) : null;
    }

    private LocalDateTime aLocalDateTime(Timestamp fecha) {
        return fecha != null ? fecha.toLocalDateTime() : null;
    }
}
