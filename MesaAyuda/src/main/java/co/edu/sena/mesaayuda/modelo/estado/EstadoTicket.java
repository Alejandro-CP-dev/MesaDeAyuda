package co.edu.sena.mesaayuda.modelo.estado;

/**
 * Patron State: cada estado del ciclo de vida del ticket (seccion 7 del
 * taller) es una clase que implementa esta interfaz y decide, ella misma,
 * a que estado puede pasar. Ticket delega aqui en vez de usar if/else o un
 * switch sobre un enum (esa es la regla clave del taller).
 *
 * OCP: los metodos default lanzan TransicionInvalidaException; cada estado
 * concreto SOLO sobreescribe las transiciones que realmente permite. Agregar
 * un estado nuevo (por ejemplo EN_ESPERA_CLIENTE) es crear una clase nueva
 * que implemente esta interfaz: los estados existentes no se tocan.
 *
 * LSP: cualquier implementacion puede sustituir a EstadoTicket en el flujo
 * de Ticket sin romperlo, ya sea devolviendo el siguiente estado o lanzando
 * la excepcion documentada.
 */
public interface EstadoTicket {

    default EstadoTicket asignar() {
        throw transicionInvalida("asignar");
    }

    default EstadoTicket iniciar() {
        throw transicionInvalida("iniciar atencion");
    }

    default EstadoTicket resolver() {
        throw transicionInvalida("resolver");
    }

    default EstadoTicket cerrar() {
        throw transicionInvalida("cerrar");
    }

    default EstadoTicket reabrir() {
        throw transicionInvalida("reabrir");
    }

    default EstadoTicket cancelar() {
        throw transicionInvalida("cancelar");
    }

    /** Nombre persistido en la columna Ticket.Estado (ej: "EN_PROCESO"). */
    String nombre();

    private TransicionInvalidaException transicionInvalida(String accion) {
        return new TransicionInvalidaException(
                "No se puede '" + accion + "' un ticket que esta en estado " + nombre());
    }
}
