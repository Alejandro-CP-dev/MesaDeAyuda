package co.edu.sena.mesaayuda.servicio.notificacion;

import co.edu.sena.mesaayuda.modelo.Usuario;

import java.util.List;

/**
 * Punto unico por el que TicketService dispara notificaciones. Recibe la
 * lista de Notificador disponibles por constructor (DIP) y los invoca a
 * todos: hoy eso significa "en aplicacion" + "correo simulado", pero
 * cambiar que canales estan activos es cosa de AppContextListener, no de
 * esta clase ni de TicketService.
 */
public class PublicadorNotificaciones {

    private final List<Notificador> notificadores;

    public PublicadorNotificaciones(List<Notificador> notificadores) {
        this.notificadores = notificadores;
    }

    public void publicar(Usuario destinatario, Long ticketId, String asunto, String mensaje) {
        for (Notificador notificador : notificadores) {
            notificador.notificar(destinatario, ticketId, asunto, mensaje);
        }
    }
}
