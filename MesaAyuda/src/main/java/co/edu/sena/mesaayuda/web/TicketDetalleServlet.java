package co.edu.sena.mesaayuda.web;

import co.edu.sena.mesaayuda.dto.TicketDTO;
import co.edu.sena.mesaayuda.dto.UsuarioDTO;
import co.edu.sena.mesaayuda.mapper.UsuarioMapper;
import co.edu.sena.mesaayuda.modelo.Rol;
import co.edu.sena.mesaayuda.modelo.Usuario;
import co.edu.sena.mesaayuda.modelo.estado.TransicionInvalidaException;
import co.edu.sena.mesaayuda.repositorio.UsuarioRepository;
import co.edu.sena.mesaayuda.servicio.ComentarioService;
import co.edu.sena.mesaayuda.servicio.ConsultaTicketService;
import co.edu.sena.mesaayuda.servicio.OperacionesAdministrador;
import co.edu.sena.mesaayuda.servicio.OperacionesAgente;
import co.edu.sena.mesaayuda.servicio.OperacionesSolicitante;
import co.edu.sena.mesaayuda.servicio.excepcion.AccesoNoAutorizadoException;
import co.edu.sena.mesaayuda.servicio.excepcion.RecursoNoEncontradoException;

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
 * Este servlet SI atiende a los tres roles (a diferencia de TicketsServlet),
 * porque el detalle de un ticket es el unico punto donde solicitante, agente y
 * admin actuan sobre el MISMO recurso. Por eso pide las tres interfaces
 * segregadas (ISP): cada "case" del switch usa solo la interfaz de rol que le
 * corresponde a esa accion, nunca una interfaz "de todo".
 *
 * Este servlet tampoco decide si una transicion es valida: eso lo resuelve el
 * patron State dentro de Ticket/EstadoTicket. Aqui solo se traduce la
 * TransicionInvalidaException en un mensaje para la vista.
 */
@WebServlet(name = "ticketDetalleServlet", urlPatterns = {"/app/ticket"})
public class TicketDetalleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = SesionUsuario.obtener(request);
        ConsultaTicketService consultaTicketService
                = (ConsultaTicketService) getServletContext().getAttribute(AppContextListener.CONSULTA_TICKET_SERVICE);
        UsuarioRepository usuarioRepository
                = (UsuarioRepository) getServletContext().getAttribute(AppContextListener.USUARIO_REPOSITORY);

        Long ticketId = Long.valueOf(request.getParameter("id"));

        try {
            TicketDTO ticket = consultaTicketService.obtenerDetalle(ticketId, usuario);
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
        ComentarioService comentarioService
                = (ComentarioService) getServletContext().getAttribute(AppContextListener.COMENTARIO_SERVICE);
        OperacionesSolicitante operacionesSolicitante
                = (OperacionesSolicitante) getServletContext().getAttribute(AppContextListener.OPERACIONES_SOLICITANTE);
        OperacionesAgente operacionesAgente
                = (OperacionesAgente) getServletContext().getAttribute(AppContextListener.OPERACIONES_AGENTE);
        OperacionesAdministrador operacionesAdministrador
                = (OperacionesAdministrador) getServletContext().getAttribute(AppContextListener.OPERACIONES_ADMINISTRADOR);

        Long ticketId = Long.valueOf(request.getParameter("id"));
        String accion = request.getParameter("accion");

        try {
            switch (accion) {
                case "comentar":
                    comentarioService.agregarComentario(ticketId, usuario, request.getParameter("texto"));
                    break;
                // ---- Acciones de SOLICITANTE: solo pueden pasar por aqui
                // metodos que existen en OperacionesSolicitante. ----
                case "cerrar":
                    operacionesSolicitante.cerrar(ticketId, usuario);
                    break;
                case "reabrir":
                    operacionesSolicitante.reabrir(ticketId, usuario);
                    break;
                // ---- Acciones de AGENTE ----
                case "iniciar":
                    operacionesAgente.iniciarAtencion(ticketId, usuario);
                    break;
                case "resolver":
                    operacionesAgente.resolver(ticketId, usuario);
                    break;
                // ---- Acciones de ADMINISTRADOR ----
                case "cancelar":
                    operacionesAdministrador.cancelar(ticketId, usuario);
                    break;
                case "reasignar":
                    Long nuevoAgenteId = Long.valueOf(request.getParameter("nuevoAgenteId"));
                    operacionesAdministrador.reasignar(ticketId, nuevoAgenteId, usuario);
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
