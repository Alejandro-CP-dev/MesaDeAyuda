package co.edu.sena.mesaayuda.servicio.excepcion;

/** Se lanza cuando se pide por Id un ticket, usuario o categoria que no existe. */
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
