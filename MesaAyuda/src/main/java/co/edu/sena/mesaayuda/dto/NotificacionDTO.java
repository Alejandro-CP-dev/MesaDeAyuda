package co.edu.sena.mesaayuda.dto;

/** Una notificacion ya lista para mostrar en la bandeja del usuario. */
public class NotificacionDTO {

    private final Long ticketId;
    private final String canal;
    private final String asunto;
    private final String mensaje;
    private final String fechaEnvio;

    public NotificacionDTO(Long ticketId, String canal, String asunto, String mensaje, String fechaEnvio) {
        this.ticketId = ticketId;
        this.canal = canal;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.fechaEnvio = fechaEnvio;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public String getCanal() {
        return canal;
    }

    public String getAsunto() {
        return asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public String getFechaEnvio() {
        return fechaEnvio;
    }
}
