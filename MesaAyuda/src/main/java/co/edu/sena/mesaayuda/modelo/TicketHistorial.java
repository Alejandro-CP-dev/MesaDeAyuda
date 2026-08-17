package co.edu.sena.mesaayuda.modelo;

import java.time.LocalDateTime;

/**
 * Bitacora de cada transicion de estado ejecutada. Se usa para demostrar en
 * sustentacion que las transiciones ocurrieron en orden valido y alimenta
 * el dashboard de metricas.
 */
public class TicketHistorial {

    private final Long id;
    private final Long ticketId;
    private final String estadoAnterior;
    private final String estadoNuevo;
    private final Long usuarioId;
    private final LocalDateTime fecha;

    public TicketHistorial(Long id, Long ticketId, String estadoAnterior, String estadoNuevo,
                            Long usuarioId, LocalDateTime fecha) {
        this.id = id;
        this.ticketId = ticketId;
        this.estadoAnterior = estadoAnterior;
        this.estadoNuevo = estadoNuevo;
        this.usuarioId = usuarioId;
        this.fecha = fecha;
    }

    public TicketHistorial(Long ticketId, String estadoAnterior, String estadoNuevo, Long usuarioId) {
        this(null, ticketId, estadoAnterior, estadoNuevo, usuarioId, LocalDateTime.now());
    }

    public Long getId() {
        return id;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public String getEstadoAnterior() {
        return estadoAnterior;
    }

    public String getEstadoNuevo() {
        return estadoNuevo;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }
}
