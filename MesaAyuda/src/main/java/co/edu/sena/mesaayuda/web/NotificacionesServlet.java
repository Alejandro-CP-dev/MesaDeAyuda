package co.edu.sena.mesaayuda.web;

import co.edu.sena.mesaayuda.dto.NotificacionDTO;
import co.edu.sena.mesaayuda.modelo.Usuario;
import co.edu.sena.mesaayuda.servicio.NotificacionService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Bandeja de notificaciones del usuario autenticado (RF-08: el sistema
 * notifica; esta es la pantalla donde el destinatario las consulta).
 */
@WebServlet(name = "notificacionesServlet", urlPatterns = {"/app/notificaciones"})
public class NotificacionesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = SesionUsuario.obtener(request);
        NotificacionService notificacionService =
                (NotificacionService) getServletContext().getAttribute(AppContextListener.NOTIFICACION_SERVICE);

        List<NotificacionDTO> notificaciones = notificacionService.listarPropias(usuario);
        request.setAttribute("notificaciones", notificaciones);
        request.getRequestDispatcher("/WEB-INF/jsp/notificaciones.jsp").forward(request, response);
    }
}
