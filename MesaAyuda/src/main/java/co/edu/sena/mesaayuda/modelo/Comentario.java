package co.edu.sena.mesaayuda.modelo;

import java.time.LocalDateTime;

/** Comentario de seguimiento sobre un ticket (RF-07). */
public class Comentario {

    private final Long id;
    private final Long ticketId;
    private final Long autorId;
    private final String texto;
    private final LocalDateTime fecha;

    public Comentario(Long id, Long ticketId, Long autorId, String texto, LocalDateTime fecha) {
        this.id = id;
        this.ticketId = ticketId;
        this.autorId = autorId;
        this.texto = texto;
        this.fecha = fecha;
    }

    public Comentario(Long ticketId, Long autorId, String texto) {
        this(null, ticketId, autorId, texto, LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public Long getAutorId() {
        return autorId;
    }

    public String getTexto() {
        return texto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
