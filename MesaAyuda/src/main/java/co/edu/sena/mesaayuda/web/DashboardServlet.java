package co.edu.sena.mesaayuda.web;

import co.edu.sena.mesaayuda.dto.MetricasDTO;
import co.edu.sena.mesaayuda.modelo.Rol;
import co.edu.sena.mesaayuda.modelo.Usuario;
import co.edu.sena.mesaayuda.servicio.ReporteService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Reto adicional: dashboard de metricas (tickets por estado, por agente y
 * SLA vencidos), solo para ADMINISTRADOR.
 *
 * SRP: este servlet solo valida el rol y traduce HTTP <-> ReporteService;
 * el calculo de las metricas vive enteramente en el servicio.
 */
@WebServlet(name = "dashboardServlet", urlPatterns = {"/app/reportes"})
public class DashboardServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Usuario usuario = SesionUsuario.obtener(request);

        if (usuario.getRol() != Rol.ADMINISTRADOR) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Solo un administrador puede ver el dashboard");
            return;
        }

        ReporteService reporteService =
                (ReporteService) getServletContext().getAttribute(AppContextListener.REPORTE_SERVICE);

        MetricasDTO metricas = reporteService.generarMetricas(usuario);
        request.setAttribute("metricas", metricas);
        request.getRequestDispatcher("/WEB-INF/jsp/dashboard.jsp").forward(request, response);
    }
}
