package co.edu.sena.mesaayuda.mapper;

import co.edu.sena.mesaayuda.dto.ComentarioDTO;
import co.edu.sena.mesaayuda.dto.TicketDTO;
import co.edu.sena.mesaayuda.modelo.Comentario;
import co.edu.sena.mesaayuda.modelo.Ticket;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Convierte Ticket (entidad, con su EstadoTicket y sus IDs de usuario) en
 * TicketDTO (strings listos para la JSP). Los nombres de solicitante/agente
 * y los comentarios ya resueltos se reciben por parametro: resolverlos
 * (consultar UsuarioRepository/ComentarioRepository) es responsabilidad de
 * TicketService, no del mapper (SRP).
 */
public final class TicketMapper {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private TicketMapper() {
    }

    public static TicketDTO aDTO(Ticket ticket, String solicitanteNombre, String agenteNombre,
                                  List<Comentario> comentarios, java.util.Map<Long, String> nombresPorAutor) {
        List<ComentarioDTO> comentariosDto = comentarios.stream()
                .map(c -> new ComentarioDTO(
                        nombresPorAutor.getOrDefault(c.getAutorId(), "Usuario"),
                        c.getTexto(),
                        c.getFecha().format(FORMATO_FECHA)))
                .collect(Collectors.toList());

        return new TicketDTO(
                ticket.getId(),
                ticket.getTitulo(),
                ticket.getDescripcion(),
                ticket.getEstado().nombre(),
                ticket.getCategoria().getNombre(),
                ticket.getPrioridad().getNombre(),
                solicitanteNombre,
                agenteNombre,
                ticket.getFechaCreacion().format(FORMATO_FECHA),
                ticket.getFechaLimiteSla() != null ? ticket.getFechaLimiteSla().format(FORMATO_FECHA) : "-",
                ticket.vencido(),
                comentariosDto);
    }
}
