package co.edu.sena.mesaayuda.repositorio;

import co.edu.sena.mesaayuda.modelo.Comentario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ComentarioRepositoryJdbc implements ComentarioRepository {

    @Override
    public Comentario guardar(Comentario comentario) {
        String sql = "INSERT INTO Comentario (TicketId, AutorId, Texto, Fecha) VALUES (?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.obtener();
             PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setLong(1, comentario.getTicketId());
            sentencia.setLong(2, comentario.getAutorId());
            sentencia.setString(3, comentario.getTexto());
            sentencia.setTimestamp(4, Timestamp.valueOf(comentario.getFecha()));
            sentencia.executeUpdate();

            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                claves.next();
                return new Comentario(claves.getLong(1), comentario.getTicketId(), comentario.getAutorId(),
                        comentario.getTexto(), comentario.getFecha());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error guardando el comentario", e);
        }
    }

    @Override
    public List<Comentario> listarPorTicket(Long ticketId) {
        String sql = "SELECT Id, TicketId, AutorId, Texto, Fecha FROM Comentario WHERE TicketId = ? ORDER BY Fecha ASC";
        List<Comentario> comentarios = new ArrayList<>();
        try (Connection conexion = ConexionBD.obtener();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setLong(1, ticketId);
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    comentarios.add(new Comentario(
                            resultado.getLong("Id"),
                            resultado.getLong("TicketId"),
                            resultado.getLong("AutorId"),
                            resultado.getString("Texto"),
                            resultado.getTimestamp("Fecha").toLocalDateTime()));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando comentarios del ticket " + ticketId, e);
        }
        return comentarios;
    }
}
