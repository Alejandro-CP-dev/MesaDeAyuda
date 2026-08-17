package co.edu.sena.mesaayuda.servicio.notificacion;

import co.edu.sena.mesaayuda.modelo.CanalNotificacion;
import co.edu.sena.mesaayuda.modelo.Notificacion;
import co.edu.sena.mesaayuda.modelo.Usuario;
import co.edu.sena.mesaayuda.repositorio.NotificacionRepository;

/**
 * Notificacion "en aplicacion": queda guardada en la tabla Notificacion y
 * el usuario la ve en su bandeja dentro del sistema.
 */
public class NotificadorEnAplicacion implements Notificador {

    private final NotificacionRepository notificacionRepository;

    public NotificadorEnAplicacion(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    @Override
    public CanalNotificacion canal() {
        return CanalNotificacion.APLICACION;
    }

    @Override
    public void notificar(Usuario destinatario, Long ticketId, String asunto, String mensaje) {
        Notificacion notificacion = new Notificacion(
                ticketId, destinatario.getId(), CanalNotificacion.APLICACION, asunto, mensaje);
        notificacionRepository.guardar(notificacion);
    }
}
