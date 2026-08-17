package co.edu.sena.mesaayuda.servicio.excepcion;

/** Se lanza cuando el correo o la contrasena no coinciden al iniciar sesion. */
public class CredencialesInvalidasException extends RuntimeException {

    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}
