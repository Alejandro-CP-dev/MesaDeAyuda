package co.edu.sena.mesaayuda.modelo;

import java.time.LocalDateTime;

/** Notificacion enviada a un usuario ante un cambio de estado (RF-08). */
public class Notificacion {

    private final Long id;
    private final Long ticketId;
    private final Long destinatarioId;
    private final CanalNotificacion canal;
    private final String asunto;
    private final String mensaje;
    private final LocalDateTime fechaEnvio;
    private final boolean leida;

    public Notificacion(Long id, Long ticketId, Long destinatarioId, CanalNotificacion canal,
                         String asunto, String mensaje, LocalDateTime fechaEnvio, boolean leida) {
        this.id = id;
        this.ticketId = ticketId;
        this.destinatarioId = destinatarioId;
        this.canal = canal;
        this.asunto = asunto;
        this.mensaje = mensaje;
        this.fechaEnvio = fechaEnvio;
        this.leida = leida;
    }

    public Notificacion(Long ticketId, Long destinatarioId, CanalNotificacion canal,
                         String asunto, String mensaje) {
        this(null, ticketId, destinatarioId, canal, asunto, mensaje, LocalDateTime.now(), false);
    }

    public Long getId() {
        return id;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public Long getDestinatarioId() {
        return destinatarioId;
    }

    public CanalNotificacion getCanal() {
        return canal;
    }

    public String getAsunto() {
        return asunto;
    }

    public String getMensaje() {
        return mensaje;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public boolean isLeida() {
        return leida;
    }
}
