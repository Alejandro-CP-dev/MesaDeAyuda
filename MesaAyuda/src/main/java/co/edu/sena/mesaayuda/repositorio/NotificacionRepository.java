package co.edu.sena.mesaayuda.repositorio;

import co.edu.sena.mesaayuda.modelo.Notificacion;

import java.util.List;

public interface NotificacionRepository {

    Notificacion guardar(Notificacion notificacion);

    List<Notificacion> listarPorDestinatario(Long destinatarioId);
}
