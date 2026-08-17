package co.edu.sena.mesaayuda.web;

import co.edu.sena.mesaayuda.dto.TicketDTO;
import co.edu.sena.mesaayuda.modelo.Categoria;
import co.edu.sena.mesaayuda.modelo.Rol;
import co.edu.sena.mesaayuda.modelo.Usuario;
import co.edu.sena.mesaayuda.repositorio.CategoriaRepository;
import co.edu.sena.mesaayuda.servicio.TicketService;
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
 * RF-05: lista tickets segun el rol de quien esta autenticado.
 * RF-02: registra un ticket nuevo (solo SOLICITANTE).
 *
 * SRP: este servlet solo orquesta HTTP <-> TicketService; ninguna regla de
 * negocio (permisos, prioridad, SLA, asignacion) vive aqui.
 */
@WebServlet(name = "ticketsServlet", urlPatterns = {"/app/tickets"})
public class TicketsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = SesionUsuario.obtener(request);
        TicketService ticketService = (TicketService) getServletContext().getAttribute(AppContextListener.TICKET_SERVICE);
        CategoriaRepository categoriaRepository =
                (CategoriaRepository) getServletContext().getAttribute(AppContextListener.CATEGORIA_REPOSITORY);

        List<TicketDTO> tickets = ticketService.listarParaUsuario(usuario);
        List<Categoria> categorias = categoriaRepository.listarTodas();

        request.setAttribute("tickets", tickets);
        request.setAttribute("categorias", categorias);
        request.getRequestDispatcher("/WEB-INF/jsp/tickets.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = SesionUsuario.obtener(request);
        TicketService ticketService = (TicketService) getServletContext().getAttribute(AppContextListener.TICKET_SERVICE);

        if (usuario.getRol() != Rol.SOLICITANTE) {
            request.setAttribute("error", "Solo un solicitante puede registrar tickets");
            doGet(request, response);
            return;
        }

        String titulo = request.getParameter("titulo");
        String descripcion = request.getParameter("descripcion");
        Long categoriaId = Long.valueOf(request.getParameter("categoriaId"));

        try {
            ticketService.crearTicket(titulo, descripcion, categoriaId, usuario);
            response.sendRedirect(request.getContextPath() + "/app/tickets");
        } catch (RecursoNoEncontradoException | AccesoNoAutorizadoException e) {
            request.setAttribute("error", e.getMessage());
            doGet(request, response);
        }
    }
}
