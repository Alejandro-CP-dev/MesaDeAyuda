package co.edu.sena.mesaayuda.repositorio;

import co.edu.sena.mesaayuda.modelo.TicketHistorial;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TicketHistorialRepositoryJdbc implements TicketHistorialRepository {

    @Override
    public void guardar(TicketHistorial historial) {
        String sql = "INSERT INTO TicketHistorial (TicketId, EstadoAnterior, EstadoNuevo, UsuarioId, Fecha) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.obtener();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setLong(1, historial.getTicketId());
            sentencia.setString(2, historial.getEstadoAnterior());
            sentencia.setString(3, historial.getEstadoNuevo());
            sentencia.setLong(4, historial.getUsuarioId());
            sentencia.setTimestamp(5, Timestamp.valueOf(historial.getFecha()));
            sentencia.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error guardando el historial del ticket " + historial.getTicketId(), e);
        }
    }

    @Override
    public List<TicketHistorial> listarPorTicket(Long ticketId) {
        String sql = "SELECT Id, TicketId, EstadoAnterior, EstadoNuevo, UsuarioId, Fecha " +
                "FROM TicketHistorial WHERE TicketId = ? ORDER BY Fecha ASC";
        List<TicketHistorial> historial = new ArrayList<>();
        try (Connection conexion = ConexionBD.obtener();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setLong(1, ticketId);
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    historial.add(new TicketHistorial(
                            resultado.getLong("Id"),
                            resultado.getLong("TicketId"),
                            resultado.getString("EstadoAnterior"),
                            resultado.getString("EstadoNuevo"),
                            resultado.getLong("UsuarioId"),
                            resultado.getTimestamp("Fecha").toLocalDateTime()));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando el historial del ticket " + ticketId, e);
        }
        return historial;
    }
}
