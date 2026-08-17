package co.edu.sena.mesaayuda.servicio.notificacion;

import co.edu.sena.mesaayuda.modelo.CanalNotificacion;
import co.edu.sena.mesaayuda.modelo.Usuario;

/**
 * Estrategia de notificacion (RF-08, Strategy/OCP). Cada canal (correo,
 * SMS, en aplicacion) es una implementacion de esta interfaz. Agregar un
 * canal nuevo (ej. WhatsApp) es crear una clase mas y sumarla a la lista en
 * AppContextListener; TicketService, que dispara las notificaciones, no
 * cambia.
 */
public interface Notificador {

    CanalNotificacion canal();

    void notificar(Usuario destinatario, Long ticketId, String asunto, String mensaje);
}
