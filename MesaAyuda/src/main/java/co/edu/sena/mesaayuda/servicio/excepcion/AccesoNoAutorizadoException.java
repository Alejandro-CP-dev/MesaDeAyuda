package co.edu.sena.mesaayuda.servicio.excepcion;

/** Se lanza cuando un usuario intenta una accion que su rol no permite (RF-05, RF-10). */
public class AccesoNoAutorizadoException extends RuntimeException {

    public AccesoNoAutorizadoException(String mensaje) {
        super(mensaje);
    }
}
