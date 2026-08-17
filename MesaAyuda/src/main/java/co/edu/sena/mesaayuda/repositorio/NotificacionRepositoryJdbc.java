package co.edu.sena.mesaayuda.repositorio;

import co.edu.sena.mesaayuda.modelo.CanalNotificacion;
import co.edu.sena.mesaayuda.modelo.Notificacion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class NotificacionRepositoryJdbc implements NotificacionRepository {

    @Override
    public Notificacion guardar(Notificacion notificacion) {
        String sql = "INSERT INTO Notificacion (TicketId, DestinatarioId, Canal, Asunto, Mensaje, FechaEnvio, Leida) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conexion = ConexionBD.obtener();
             PreparedStatement sentencia = conexion.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setLong(1, notificacion.getTicketId());
            sentencia.setLong(2, notificacion.getDestinatarioId());
            sentencia.setString(3, notificacion.getCanal().name());
            sentencia.setString(4, notificacion.getAsunto());
            sentencia.setString(5, notificacion.getMensaje());
            sentencia.setTimestamp(6, Timestamp.valueOf(notificacion.getFechaEnvio()));
            sentencia.setBoolean(7, notificacion.isLeida());
            sentencia.executeUpdate();

            try (ResultSet claves = sentencia.getGeneratedKeys()) {
                claves.next();
                return new Notificacion(claves.getLong(1), notificacion.getTicketId(), notificacion.getDestinatarioId(),
                        notificacion.getCanal(), notificacion.getAsunto(), notificacion.getMensaje(),
                        notificacion.getFechaEnvio(), notificacion.isLeida());
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error guardando la notificacion", e);
        }
    }

    @Override
    public List<Notificacion> listarPorDestinatario(Long destinatarioId) {
        String sql = "SELECT Id, TicketId, DestinatarioId, Canal, Asunto, Mensaje, FechaEnvio, Leida " +
                "FROM Notificacion WHERE DestinatarioId = ? ORDER BY FechaEnvio DESC";
        List<Notificacion> notificaciones = new ArrayList<>();
        try (Connection conexion = ConexionBD.obtener();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setLong(1, destinatarioId);
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    notificaciones.add(new Notificacion(
                            resultado.getLong("Id"),
                            resultado.getLong("TicketId"),
                            resultado.getLong("DestinatarioId"),
                            CanalNotificacion.valueOf(resultado.getString("Canal")),
                            resultado.getString("Asunto"),
                            resultado.getString("Mensaje"),
                            resultado.getTimestamp("FechaEnvio").toLocalDateTime(),
                            resultado.getBoolean("Leida")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando notificaciones de " + destinatarioId, e);
        }
        return notificaciones;
    }
}
