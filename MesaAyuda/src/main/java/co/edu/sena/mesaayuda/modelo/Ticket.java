package co.edu.sena.mesaayuda.modelo;

import co.edu.sena.mesaayuda.modelo.estado.EstadoTicket;
import co.edu.sena.mesaayuda.modelo.estado.Nuevo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Ticket de soporte. El ciclo de vida (campo "estado") NO se controla con
 * if/else aqui: cada transicion se delega al objeto EstadoTicket actual
 * (patron State). Ticket solo sabe pedirle al estado que transicione y,
 * si el estado lo permite, actualiza sus propios datos (fecha, agente).
 *
 * Si la transicion no es valida, el propio EstadoTicket lanza
 * TransicionInvalidaException y este objeto queda sin cambios.
 */
public class Ticket {

    private final Long id;
    private final String titulo;
    private final String descripcion;
    private EstadoTicket estado;
    private final LocalDateTime fechaCreacion;
    private LocalDateTime fechaAsignacion;
    private LocalDateTime fechaResolucion;
    private LocalDateTime fechaCierre;
    private LocalDateTime fechaLimiteSla;
    private final Categoria categoria;
    private final Prioridad prioridad;
    private final Long solicitanteId;
    private Long agenteId;
    private final List<Comentario> comentarios = new ArrayList<>();

    /** Constructor para un ticket nuevo (todavia no persistido). */
    public Ticket(String titulo, String descripcion, Categoria categoria,
                   Prioridad prioridad, Long solicitanteId) {
        this(null, titulo, descripcion, Nuevo.getInstancia(), LocalDateTime.now(),
                null, null, null, null, categoria, prioridad, solicitanteId, null);
    }

    /** Constructor completo, usado por el mapper al reconstruir desde la BD. */
    public Ticket(Long id, String titulo, String descripcion, EstadoTicket estado,
                   LocalDateTime fechaCreacion, LocalDateTime fechaAsignacion,
                   LocalDateTime fechaResolucion, LocalDateTime fechaCierre,
                   LocalDateTime fechaLimiteSla, Categoria categoria, Prioridad prioridad,
                   Long solicitanteId, Long agenteId) {
        this.id = id;
        this.titulo = Objects.requireNonNull(titulo, "El titulo es obligatorio");
        this.descripcion = Objects.requireNonNull(descripcion, "La descripcion es obligatoria");
        this.estado = Objects.requireNonNull(estado, "El estado es obligatorio");
        this.fechaCreacion = fechaCreacion;
        this.fechaAsignacion = fechaAsignacion;
        this.fechaResolucion = fechaResolucion;
        this.fechaCierre = fechaCierre;
        this.fechaLimiteSla = fechaLimiteSla;
        this.categoria = Objects.requireNonNull(categoria, "La categoria es obligatoria");
        this.prioridad = Objects.requireNonNull(prioridad, "La prioridad es obligatoria");
        this.solicitanteId = Objects.requireNonNull(solicitanteId, "El solicitante es obligatorio");
        this.agenteId = agenteId;
    }

    // ---- Transiciones del ciclo de vida (RF-06): delegan al patron State ----

    /** El sistema asigna un agente (RF-04). NUEVO -> ASIGNADO. */
    public void asignar(Long agenteId) {
        this.estado = estado.asignar();
        this.agenteId = Objects.requireNonNull(agenteId, "Se requiere un agente para asignar");
        this.fechaAsignacion = LocalDateTime.now();
    }

    /** El agente inicia atencion. ASIGNADO -> EN_PROCESO. */
    public void iniciar() {
        this.estado = estado.iniciar();
    }

    /** El agente resuelve el ticket. EN_PROCESO -> RESUELTO. */
    public void resolver() {
        this.estado = estado.resolver();
        this.fechaResolucion = LocalDateTime.now();
    }

    /** El solicitante confirma y cierra. RESUELTO -> CERRADO. */
    public void cerrar() {
        this.estado = estado.cerrar();
        this.fechaCierre = LocalDateTime.now();
    }

    /** El solicitante reabre porque el problema persiste. RESUELTO -> EN_PROCESO. */
    public void reabrir() {
        this.estado = estado.reabrir();
        this.fechaResolucion = null;
    }

    /** El administrador cancela. Cualquier estado no cerrado -> CANCELADO. */
    public void cancelar() {
        this.estado = estado.cancelar();
    }

    /** Reasignacion administrativa (RF-10): no cambia de estado, solo de agente. */
    public void reasignar(Long nuevoAgenteId) {
        this.agenteId = Objects.requireNonNull(nuevoAgenteId, "Se requiere un agente para reasignar");
    }

    public void agregarComentario(Comentario comentario) {
        comentarios.add(Objects.requireNonNull(comentario));
    }

    public void definirFechaLimiteSla(LocalDateTime fechaLimiteSla) {
        this.fechaLimiteSla = fechaLimiteSla;
    }

    // ---- Getters ----

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public EstadoTicket getEstado() {
        return estado;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public LocalDateTime getFechaResolucion() {
        return fechaResolucion;
    }

    public LocalDateTime getFechaCierre() {
        return fechaCierre;
    }

    public LocalDateTime getFechaLimiteSla() {
        return fechaLimiteSla;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public Long getSolicitanteId() {
        return solicitanteId;
    }

    public Long getAgenteId() {
        return agenteId;
    }

    public List<Comentario> getComentarios() {
        return Collections.unmodifiableList(comentarios);
    }

    public boolean vencido() {
        return fechaLimiteSla != null
                && LocalDateTime.now().isAfter(fechaLimiteSla)
                && !"CERRADO".equals(estado.nombre())
                && !"RESUELTO".equals(estado.nombre())
                && !"CANCELADO".equals(estado.nombre());
    }
}
