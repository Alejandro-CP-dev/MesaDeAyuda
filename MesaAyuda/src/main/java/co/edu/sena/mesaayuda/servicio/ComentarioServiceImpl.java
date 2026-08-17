package co.edu.sena.mesaayuda.servicio;

import co.edu.sena.mesaayuda.modelo.Comentario;
import co.edu.sena.mesaayuda.modelo.Rol;
import co.edu.sena.mesaayuda.modelo.Ticket;
import co.edu.sena.mesaayuda.modelo.Usuario;
import co.edu.sena.mesaayuda.repositorio.ComentarioRepository;
import co.edu.sena.mesaayuda.repositorio.TicketRepository;
import co.edu.sena.mesaayuda.servicio.excepcion.AccesoNoAutorizadoException;
import co.edu.sena.mesaayuda.servicio.excepcion.RecursoNoEncontradoException;

import java.util.Objects;

public class ComentarioServiceImpl implements ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final TicketRepository ticketRepository;

    public ComentarioServiceImpl(ComentarioRepository comentarioRepository, TicketRepository ticketRepository) {
        this.comentarioRepository = comentarioRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    public void agregarComentario(Long ticketId, Usuario autor, String texto) {
        if (texto == null || texto.isBlank()) {
            throw new IllegalArgumentException("El comentario no puede estar vacio");
        }

        Ticket ticket = ticketRepository.buscarPorId(ticketId)
                .orElseThrow(() -> new RecursoNoEncontradoException("El ticket " + ticketId + " no existe"));

        boolean esSolicitante = Objects.equals(ticket.getSolicitanteId(), autor.getId());
        boolean esAgenteAsignado = Objects.equals(ticket.getAgenteId(), autor.getId());
        boolean esAdmin = autor.getRol() == Rol.ADMINISTRADOR;

        if (!esSolicitante && !esAgenteAsignado && !esAdmin) {
            throw new AccesoNoAutorizadoException("No puedes comentar en un ticket que no es tuyo");
        }

        comentarioRepository.guardar(new Comentario(ticketId, autor.getId(), texto.trim()));
    }
}
