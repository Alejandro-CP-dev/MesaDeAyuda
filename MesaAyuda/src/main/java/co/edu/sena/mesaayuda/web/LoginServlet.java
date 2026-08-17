package co.edu.sena.mesaayuda.web;

import co.edu.sena.mesaayuda.modelo.Usuario;
import co.edu.sena.mesaayuda.servicio.AutenticacionService;
import co.edu.sena.mesaayuda.servicio.excepcion.CredencialesInvalidasException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * RF-01: pantalla e inicio de sesion. SRP: solo maneja el flujo HTTP; la
 * verificacion de credenciales vive en AutenticacionService.
 */
@WebServlet(name = "loginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (SesionUsuario.obtener(request) != null) {
            response.sendRedirect(request.getContextPath() + "/app/tickets");
            return;
        }
        request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AutenticacionService autenticacionService =
                (AutenticacionService) getServletContext().getAttribute(AppContextListener.AUTENTICACION_SERVICE);

        String correo = request.getParameter("correo");
        String password = request.getParameter("password");

        try {
            Usuario usuario = autenticacionService.autenticar(correo, password);
            SesionUsuario.iniciarSesion(request, usuario);
            response.sendRedirect(request.getContextPath() + "/app/tickets");
        } catch (CredencialesInvalidasException e) {
            request.setAttribute("error", e.getMessage());
            request.setAttribute("correo", correo);
            request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        }
    }
}
