package co.edu.sena.mesaayuda.modelo;

/**
 * Se lanza cuando el codigo OTP que el solicitante escribe para cerrar un
 * ticket no coincide con el que se genero al resolverlo (reto adicional:
 * "codigo OTP para que el solicitante confirme el cierre").
 *
 * Vive en el paquete modelo (igual que TransicionInvalidaException vive en
 * modelo.estado): es una regla del propio dominio Ticket, no del servicio
 * que lo orquesta. Poner esta excepcion en servicio.excepcion habria hecho
 * que modelo dependiera de servicio -- justo al reves de como debe ser.
 */
public class CodigoCierreInvalidoException extends RuntimeException {

    public CodigoCierreInvalidoException(String mensaje) {
        super(mensaje);
    }
}
