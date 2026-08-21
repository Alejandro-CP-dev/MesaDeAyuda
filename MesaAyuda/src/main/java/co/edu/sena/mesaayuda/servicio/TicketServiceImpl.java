package co.edu.sena.mesaayuda.servicio;

import co.edu.sena.mesaayuda.dto.TicketDTO;
import co.edu.sena.mesaayuda.mapper.TicketMapper;
import co.edu.sena.mesaayuda.modelo.Categoria;
import co.edu.sena.mesaayuda.modelo.Comentario;
import co.edu.sena.mesaayuda.modelo.Prioridad;
import co.edu.sena.mesaayuda.modelo.Rol;
import co.edu.sena.mesaayuda.modelo.Ticket;
import co.edu.sena.mesaayuda.modelo.TicketHistorial;
import co.edu.sena.mesaayuda.modelo.Usuario;
import co.edu.sena.mesaayuda.repositorio.CategoriaRepository;
import co.edu.sena.mesaayuda.repositorio.ComentarioRepository;
import co.edu.sena.mesaayuda.repositorio.TicketHistorialRepository;
import co.edu.sena.mesaayuda.repositorio.TicketRepository;
import co.edu.sena.mesaayuda.repositorio.UsuarioRepository;
import co.edu.sena.mesaayuda.servicio.asignacion.EstrategiaAsignacion;
import co.edu.sena.mesaayuda.servicio.excepcion.AccesoNoAutorizadoException;
import co.edu.sena.mesaayuda.servicio.excepcion.RecursoNoEncontradoException;
import co.edu.sena.mesaayuda.servicio.notificacion.PublicadorNotificaciones;
import co.edu.sena.mesaayuda.servicio.sla.EstrategiaSla;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Implementacion unica que resuelve las tres interfaces segregadas por rol
 * (ISP): {@link OperacionesSolicitante}, {@link OperacionesAgente} y
 * {@link OperacionesAdministrador}, ademas de la lectura comun
 * {@link ConsultaTicketService}. Que la implementacion sea una sola clase
 * es una decision valida (LSP: cada interfaz sigue siendo sustituible por
 * esta clase sin sorpresas) — lo que importa para ISP es que cada
 * CONSUMIDOR (cada Servlet) dependa solo de la interfaz de su rol, nunca
 * de las tres juntas ni de esta clase concreta.
 *
 * Coordina el patron State (Ticket delega las transiciones en su
 * EstadoTicket) con las estrategias de SLA, asignacion y notificacion, y
 * deja constancia de cada cambio en TicketHistorial. No hay ni un if/else
 * de estados aqui: eso vive en las clases de modelo.estado.
 */
public class TicketServiceImpl implements OperacionesSolicitante, OperacionesAgente, OperacionesAdministrador {

    private final TicketRepository ticketRepository;
    private final CategoriaRepository categoriaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ComentarioRepository comentarioRepository;
    private final TicketHistorialRepository historialRepository;
    private final PrioridadService prioridadService;
    private final EstrategiaSla estrategiaSla;
    private final EstrategiaAsignacion estrategiaAsignacion;
    private final PublicadorNotificaciones notificaciones;

    public TicketServiceImpl(TicketRepository ticketRepository, CategoriaRepository categoriaRepository,
                              UsuarioRepository usuarioRepository, ComentarioRepository comentarioRepository,
                              TicketHistorialRepository historialRepository, PrioridadService prioridadService,
                              EstrategiaSla estrategiaSla, EstrategiaAsignacion estrategiaAsignacion,
                              PublicadorNotificaciones notificaciones) {
        this.ticketRepository = ticketRepository;
        this.categoriaRepository = categoriaRepository;
        this.usuarioRepository = usuarioRepository;
        this.comentarioRepository = comentarioRepository;
        this.historialRepository = historialRepository;
        this.prioridadService = prioridadService;
        this.estrategiaSla = estrategiaSla;
        this.estrategiaAsignacion = estrategiaAsignacion;
        this.notificaciones = notificaciones;
    }

    @Override
    public TicketDTO crearTicket(String titulo, String descripcion, Long categoriaId, Usuario solicitante) {
        Categoria categoria = categoriaRepository.buscarPorId(categoriaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("La categoria " + categoriaId + " no existe"));

        // RF-03: prioridad automatica segun categoria/palabras clave.
        Prioridad prioridad = prioridadService.determinarPrioridad(titulo, descripcion, categoria);

        Ticket ticket = new Ticket(titulo, descripcion, categoria, prioridad, solicitante.getId());
        ticket.definirFechaLimiteSla(estrategiaSla.calcularFechaLimite(prioridad, ticket.getFechaCreacion()));

        ticket = ticketRepository.guardar(ticket);
        historialRepository.guardar(new TicketHistorial(ticket.getId(), null, ticket.getEstado().nombre(), solicitante.getId()));

        // RF-04: el sistema intenta asignar agente automaticamente al crear.
        List<Usuario> agentes = usuarioRepository.listarPorRol(Rol.AGENTE);
        if (!agentes.isEmpty()) {
            String estadoAnterior = ticket.getEstado().nombre();
            Usuario agenteElegido = estrategiaAsignacion.elegirAgente(ticket, agentes);
            ticket.asignar(agenteElegido.getId());
            ticketRepository.actualizar(ticket);
            historialRepository.guardar(new TicketHistorial(ticket.getId(), estadoAnterior, ticket.getEstado().nombre(), solicitante.getId()));

            notificaciones.publicar(solicitante, ticket.getId(), "Ticket #" + ticket.getId() + " asignado",
                    "Tu ticket \"" + ticket.getTitulo() + "\" fue asignado a " + agenteElegido.getNombre() + ".");
        }

        return toDTO(ticket, false);
    }

    @Override
    public void iniciarAtencion(Long ticketId, Usuario agente) {
        Ticket ticket = obtenerTicketPropioDeAgente(ticketId, agente);
        transicionar(ticket, agente.getId(), Ticket::iniciar);
        notificarSolicitante(ticket, "Ticket #" + ticket.getId() + " en proceso",
                agente.getNombre() + " comenzo a atender tu ticket.");
    }

    @Override
    public void resolver(Long ticketId, Usuario agente) {
        Ticket ticket = obtenerTicketPropioDeAgente(ticketId, agente);
        transicionar(ticket, agente.getId(), Ticket::resolver);
        notificarSolicitante(ticket, "Ticket #" + ticket.getId() + " resuelto",
                "Tu ticket fue marcado como resuelto. Si el problema persiste, puedes reabrirlo.");
    }

    @Override
    public void cerrar(Long ticketId, Usuario solicitante) {
        Ticket ticket = obtenerTicketPropioDeSolicitante(ticketId, solicitante);
        transicionar(ticket, solicitante.getId(), Ticket::cerrar);
        // RF-08 exige notificar al solicitante en CADA cambio de estado,
        // incluido este que el mismo solicitante provoco: deja constancia
        // formal del cierre (util como comprobante), ademas de avisar al
        // agente de que el caso quedo cerrado.
        notificarSolicitante(ticket, "Ticket #" + ticket.getId() + " cerrado",
                "Confirmaste y cerraste tu ticket. Gracias por usar la Mesa de Ayuda.");
        notificarAgenteSiExiste(ticket, "Ticket #" + ticket.getId() + " cerrado",
                solicitante.getNombre() + " confirmo y cerro el ticket.");
    }

    @Override
    public void reabrir(Long ticketId, Usuario solicitante) {
        Ticket ticket = obtenerTicketPropioDeSolicitante(ticketId, solicitante);
        transicionar(ticket, solicitante.getId(), Ticket::reabrir);
        // Mismo motivo que en cerrar(): RF-08 pide notificar al solicitante
        // en cada transicion, sin excepcion para las que el mismo origina.
        notificarSolicitante(ticket, "Ticket #" + ticket.getId() + " reabierto",
                "Reabriste tu ticket. El agente retomara la atencion.");
        notificarAgenteSiExiste(ticket, "Ticket #" + ticket.getId() + " reabierto",
                solicitante.getNombre() + " reabrio el ticket: el problema persiste.");
    }

    @Override
    public void cancelar(Long ticketId, Usuario administrador) {
        exigirRol(administrador, Rol.ADMINISTRADOR);
        Ticket ticket = buscarTicket(ticketId);
        transicionar(ticket, administrador.getId(), Ticket::cancelar);
        notificarSolicitante(ticket, "Ticket #" + ticket.getId() + " cancelado",
                "Tu ticket fue cancelado por un administrador.");
        notificarAgenteSiExiste(ticket, "Ticket #" + ticket.getId() + " cancelado", "El administrador cancelo el ticket.");
    }

    @Override
    public void reasignar(Long ticketId, Long nuevoAgenteId, Usuario administrador) {
        exigirRol(administrador, Rol.ADMINISTRADOR);
        Ticket ticket = buscarTicket(ticketId);
        Usuario nuevoAgente = usuarioRepository.buscarPorId(nuevoAgenteId)
                .filter(u -> u.getRol() == Rol.AGENTE)
                .orElseThrow(() -> new RecursoNoEncontradoException("El agente " + nuevoAgenteId + " no existe"));

        ticket.reasignar(nuevoAgenteId);
        ticketRepository.actualizar(ticket);

        notificaciones.publicar(nuevoAgente, ticket.getId(), "Ticket #" + ticket.getId() + " reasignado",
                "Se te reasigno el ticket \"" + ticket.getTitulo() + "\".");
    }

    @Override
    public List<TicketDTO> listarParaUsuario(Usuario usuarioActual) {
        List<Ticket> tickets;
        switch (usuarioActual.getRol()) {
            case SOLICITANTE:
                tickets = ticketRepository.listarPorSolicitante(usuarioActual.getId());
                break;
            case AGENTE:
                tickets = ticketRepository.listarPorAgente(usuarioActual.getId());
                break;
            case ADMINISTRADOR:
                tickets = ticketRepository.listarTodos();
                break;
            default:
                throw new AccesoNoAutorizadoException("Rol no reconocido");
        }
        return tickets.stream().map(t -> toDTO(t, false)).collect(Collectors.toList());
    }

    @Override
    public TicketDTO obtenerDetalle(Long ticketId, Usuario usuarioActual) {
        Ticket ticket = buscarTicket(ticketId);
        exigirAccesoAlTicket(ticket, usuarioActual);
        return toDTO(ticket, true);
    }

    // ---- Ayudas privadas ----

    /** Contrato funcional para aplicar una transicion del patron State sobre el Ticket. */
    @FunctionalInterface
    private interface Transicion {
        void aplicar(Ticket ticket);
    }

    private void transicionar(Ticket ticket, Long usuarioId, Transicion transicion) {
        String estadoAnterior = ticket.getEstado().nombre();
        transicion.aplicar(ticket);
        ticketRepository.actualizar(ticket);
        historialRepository.guardar(new TicketHistorial(ticket.getId(), estadoAnterior, ticket.getEstado().nombre(), usuarioId));
    }

    private Ticket buscarTicket(Long id) {
        return ticketRepository.buscarPorId(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("El ticket " + id + " no existe"));
    }

    private Ticket obtenerTicketPropioDeAgente(Long ticketId, Usuario agente) {
        exigirRol(agente, Rol.AGENTE);
        Ticket ticket = buscarTicket(ticketId);
        if (!Objects.equals(ticket.getAgenteId(), agente.getId())) {
            throw new AccesoNoAutorizadoException("Este ticket no esta asignado a " + agente.getNombre());
        }
        return ticket;
    }

    private Ticket obtenerTicketPropioDeSolicitante(Long ticketId, Usuario solicitante) {
        exigirRol(solicitante, Rol.SOLICITANTE);
        Ticket ticket = buscarTicket(ticketId);
        if (!Objects.equals(ticket.getSolicitanteId(), solicitante.getId())) {
            throw new AccesoNoAutorizadoException("Este ticket no pertenece a " + solicitante.getNombre());
        }
        return ticket;
    }

    private void exigirAccesoAlTicket(Ticket ticket, Usuario usuario) {
        boolean permitido = usuario.getRol() == Rol.ADMINISTRADOR
                || Objects.equals(ticket.getSolicitanteId(), usuario.getId())
                || Objects.equals(ticket.getAgenteId(), usuario.getId());
        if (!permitido) {
            throw new AccesoNoAutorizadoException("No tienes acceso a este ticket");
        }
    }

    private void exigirRol(Usuario usuario, Rol rolRequerido) {
        if (usuario.getRol() != rolRequerido) {
            throw new AccesoNoAutorizadoException("Esta accion requiere el rol " + rolRequerido);
        }
    }

    private void notificarSolicitante(Ticket ticket, String asunto, String mensaje) {
        usuarioRepository.buscarPorId(ticket.getSolicitanteId())
                .ifPresent(solicitante -> notificaciones.publicar(solicitante, ticket.getId(), asunto, mensaje));
    }

    private void notificarAgenteSiExiste(Ticket ticket, String asunto, String mensaje) {
        if (ticket.getAgenteId() != null) {
            usuarioRepository.buscarPorId(ticket.getAgenteId())
                    .ifPresent(agente -> notificaciones.publicar(agente, ticket.getId(), asunto, mensaje));
        }
    }

    private TicketDTO toDTO(Ticket ticket, boolean incluirComentarios) {
        String solicitanteNombre = usuarioRepository.buscarPorId(ticket.getSolicitanteId())
                .map(Usuario::getNombre).orElse("Desconocido");
        String agenteNombre = ticket.getAgenteId() == null ? "Sin asignar"
                : usuarioRepository.buscarPorId(ticket.getAgenteId()).map(Usuario::getNombre).orElse("Desconocido");

        List<Comentario> comentarios = incluirComentarios
                ? comentarioRepository.listarPorTicket(ticket.getId())
                : List.of();

        Map<Long, String> nombresPorAutor = new HashMap<>();
        for (Comentario comentario : comentarios) {
            nombresPorAutor.computeIfAbsent(comentario.getAutorId(),
                    id -> usuarioRepository.buscarPorId(id).map(Usuario::getNombre).orElse("Usuario"));
        }

        return TicketMapper.aDTO(ticket, solicitanteNombre, agenteNombre, comentarios, nombresPorAutor);
    }
}
