package co.edu.sena.mesaayuda.dto;

import java.util.Collections;
import java.util.List;

/**
 * Vista aplanada de un Ticket para las JSP. Las JSP nunca reciben la
 * entidad Ticket ni el objeto EstadoTicket: solo strings y numeros ya
 * listos para mostrar. Asi la logica de negocio se queda en servicio/modelo
 * y la JSP no necesita saber nada del patron State.
 */
public class TicketDTO {

    private final Long id;
    private final String titulo;
    private final String descripcion;
    private final String estado;
    private final String categoria;
    private final String prioridad;
    private final String solicitanteNombre;
    private final String agenteNombre;
    private final String fechaCreacion;
    private final String fechaLimiteSla;
    private final boolean vencido;
    private final List<ComentarioDTO> comentarios;

    public TicketDTO(Long id, String titulo, String descripcion, String estado, String categoria,
                      String prioridad, String solicitanteNombre, String agenteNombre,
                      String fechaCreacion, String fechaLimiteSla, boolean vencido,
                      List<ComentarioDTO> comentarios) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.categoria = categoria;
        this.prioridad = prioridad;
        this.solicitanteNombre = solicitanteNombre;
        this.agenteNombre = agenteNombre;
        this.fechaCreacion = fechaCreacion;
        this.fechaLimiteSla = fechaLimiteSla;
        this.vencido = vencido;
        this.comentarios = comentarios == null ? Collections.emptyList() : comentarios;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public String getSolicitanteNombre() {
        return solicitanteNombre;
    }

    public String getAgenteNombre() {
        return agenteNombre;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public String getFechaLimiteSla() {
        return fechaLimiteSla;
    }

    public boolean isVencido() {
        return vencido;
    }

    public List<ComentarioDTO> getComentarios() {
        return comentarios;
    }
}
