package co.edu.sena.mesaayuda.servicio.notificacion;

import co.edu.sena.mesaayuda.modelo.CanalNotificacion;
import co.edu.sena.mesaayuda.modelo.Notificacion;
import co.edu.sena.mesaayuda.modelo.Usuario;
import co.edu.sena.mesaayuda.repositorio.NotificacionRepository;

import java.util.logging.Logger;

/** Notificacion por SMS, simulada de la misma forma que NotificadorCorreo. */
public class NotificadorSms implements Notificador {

    private static final Logger LOG = Logger.getLogger(NotificadorSms.class.getName());

    private final NotificacionRepository notificacionRepository;

    public NotificadorSms(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    @Override
    public CanalNotificacion canal() {
        return CanalNotificacion.SMS;
    }

    @Override
    public void notificar(Usuario destinatario, Long ticketId, String asunto, String mensaje) {
        LOG.info(() -> "SMS simulado a " + destinatario.getNombre() + ": " + asunto);
        Notificacion notificacion = new Notificacion(
                ticketId, destinatario.getId(), CanalNotificacion.SMS, asunto, mensaje);
        notificacionRepository.guardar(notificacion);
    }
}
