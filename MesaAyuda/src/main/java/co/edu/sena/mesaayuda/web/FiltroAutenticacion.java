package co.edu.sena.mesaayuda.web;

import co.edu.sena.mesaayuda.modelo.Usuario;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Exige sesion iniciada para cualquier URL bajo /app/*. Las paginas
 * publicas (login, css) quedan fuera del patron del filtro.
 *
 * SRP: la verificacion de sesion vive en un unico lugar, no repetida al
 * inicio de cada servlet.
 */
@WebFilter(urlPatterns = {"/app/*"})
public class FiltroAutenticacion implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        // Sin inicializacion adicional.
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        Usuario usuario = SesionUsuario.obtener(httpRequest);
        if (usuario == null) {
            httpResponse.sendRedirect(httpRequest.getContextPath() + "/login");
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Sin recursos que liberar.
    }
}
