package co.edu.sena.mesaayuda.modelo.estado;

/**
 * Se lanza cuando se pide una transicion que el estado actual del ticket no
 * permite (por ejemplo, cerrar un ticket NUEVO sin resolverlo). RuntimeException
 * porque es una violacion de regla de negocio, no un error recuperable de
 * bajo nivel: el servicio la deja subir hasta el Servlet para mostrar el
 * mensaje al usuario.
 */
public class TransicionInvalidaException extends RuntimeException {

    public TransicionInvalidaException(String mensaje) {
        super(mensaje);
    }
}
