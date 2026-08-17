package co.edu.sena.mesaayuda.web;

import co.edu.sena.mesaayuda.dto.TicketDTO;
import co.edu.sena.mesaayuda.dto.UsuarioDTO;
import co.edu.sena.mesaayuda.mapper.UsuarioMapper;
import co.edu.sena.mesaayuda.modelo.Rol;
import co.edu.sena.mesaayuda.modelo.Usuario;
import co.edu.sena.mesaayuda.repositorio.UsuarioRepository;
import co.edu.sena.mesaayuda.servicio.ComentarioService;
import co.edu.sena.mesaayuda.servicio.TicketService;
import co.edu.sena.mesaayuda.servicio.excepcion.AccesoNoAutorizadoException;
import co.edu.sena.mesaayuda.servicio.excepcion.RecursoNoEncontradoException;
import co.edu.sena.mesaayuda.modelo.estado.TransicionInvalidaException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Detalle de un ticket: mostrarlo (con comentarios), agregar comentarios
 * (RF-07), cambiar de estado (RF-06) y reasignar agente (RF-10).
 *
 * Todas las acciones de estado llegan por POST con el parametro "accion".
 * Este servlet NO decide si una transicion es valida: eso lo resuelve el
 * patron State dentro de TicketService/Ticket. Aqui solo se traduce la
 * TransicionInvalidaException en un mensaje para la vista.
 */
@WebServlet(name = "ticketDetalleServlet", urlPatterns = {"/app/ticket"})
public class TicketDetalleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = SesionUsuario.obtener(request);
        TicketService ticketService = (TicketService) getServletContext().getAttribute(AppContextListener.TICKET_SERVICE);
        UsuarioRepository usuarioRepository =
                (UsuarioRepository) getServletContext().getAttribute(AppContextListener.USUARIO_REPOSITORY);

        Long ticketId = Long.valueOf(request.getParameter("id"));

        try {
            TicketDTO ticket = ticketService.obtenerDetalle(ticketId, usuario);
            request.setAttribute("ticket", ticket);

            if (usuario.getRol() == Rol.ADMINISTRADOR) {
                List<UsuarioDTO> agentes = UsuarioMapper.aDTO(usuarioRepository.listarPorRol(Rol.AGENTE));
                request.setAttribute("agentes", agentes);
            }

            request.getRequestDispatcher("/WEB-INF/jsp/ticket-detalle.jsp").forward(request, response);
        } catch (RecursoNoEncontradoException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (AccesoNoAutorizadoException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = SesionUsuario.obtener(request);
        TicketService ticketService = (TicketService) getServletContext().getAttribute(AppContextListener.TICKET_SERVICE);
        ComentarioService comentarioService =
                (ComentarioService) getServletContext().getAttribute(AppContextListener.COMENTARIO_SERVICE);

        Long ticketId = Long.valueOf(request.getParameter("id"));
        String accion = request.getParameter("accion");

        try {
            switch (accion) {
                case "comentar":
                    comentarioService.agregarComentario(ticketId, usuario, request.getParameter("texto"));
                    break;
                case "iniciar":
                    ticketService.iniciarAtencion(ticketId, usuario);
                    break;
                case "resolver":
                    ticketService.resolver(ticketId, usuario);
                    break;
                case "cerrar":
                    ticketService.cerrar(ticketId, usuario);
                    break;
                case "reabrir":
                    ticketService.reabrir(ticketId, usuario);
                    break;
                case "cancelar":
                    ticketService.cancelar(ticketId, usuario);
                    break;
                case "reasignar":
                    Long nuevoAgenteId = Long.valueOf(request.getParameter("nuevoAgenteId"));
                    ticketService.reasignar(ticketId, nuevoAgenteId, usuario);
                    break;
                default:
                    throw new IllegalArgumentException("Accion no reconocida: " + accion);
            }
            response.sendRedirect(request.getContextPath() + "/app/ticket?id=" + ticketId);
        } catch (TransicionInvalidaException | AccesoNoAutorizadoException | IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        } catch (RecursoNoEncontradoException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }
}
