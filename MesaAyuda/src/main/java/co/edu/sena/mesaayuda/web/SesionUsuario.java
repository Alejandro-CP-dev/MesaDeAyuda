package co.edu.sena.mesaayuda.web;

import co.edu.sena.mesaayuda.modelo.Usuario;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Punto unico para leer/guardar el usuario autenticado en la sesion HTTP.
 * Evita repetir literales de atributo de sesion ("usuario") por todos los
 * servlets (DRY).
 */
public final class SesionUsuario {

    private static final String ATRIBUTO_USUARIO = "usuarioAutenticado";

    private SesionUsuario() {
    }

    public static void iniciarSesion(HttpServletRequest request, Usuario usuario) {
        request.getSession(true).setAttribute(ATRIBUTO_USUARIO, usuario);
    }

    public static Usuario obtener(HttpServletRequest request) {
        HttpSession sesion = request.getSession(false);
        return sesion == null ? null : (Usuario) sesion.getAttribute(ATRIBUTO_USUARIO);
    }

    public static void cerrarSesion(HttpServletRequest request) {
        HttpSession sesion = request.getSession(false);
        if (sesion != null) {
            sesion.invalidate();
        }
    }
}
