package co.edu.sena.mesaayuda.servicio.notificacion;

import co.edu.sena.mesaayuda.modelo.CanalNotificacion;
import co.edu.sena.mesaayuda.modelo.Notificacion;
import co.edu.sena.mesaayuda.modelo.Usuario;
import co.edu.sena.mesaayuda.repositorio.NotificacionRepository;

import java.util.logging.Logger;

/**
 * Notificacion por correo. En este taller se simula (queda registrada en
 * Notificacion y se deja constancia en el log) en vez de enviar un correo
 * real; el reto adicional del enunciado sugiere reemplazar el envio real
 * con JavaMail sin tocar el resto del sistema (OCP).
 */
public class NotificadorCorreo implements Notificador {

    private static final Logger LOG = Logger.getLogger(NotificadorCorreo.class.getName());

    private final NotificacionRepository notificacionRepository;

    public NotificadorCorreo(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    @Override
    public CanalNotificacion canal() {
        return CanalNotificacion.CORREO;
    }

    @Override
    public void notificar(Usuario destinatario, Long ticketId, String asunto, String mensaje) {
        LOG.info(() -> "Correo simulado a " + destinatario.getCorreo() + ": " + asunto);
        Notificacion notificacion = new Notificacion(
                ticketId, destinatario.getId(), CanalNotificacion.CORREO, asunto, mensaje);
        notificacionRepository.guardar(notificacion);
    }
}
