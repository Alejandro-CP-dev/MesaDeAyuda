package co.edu.sena.mesaayuda.modelo.estado;

/**
 * Estado final administrativo. Igual que Cerrado, no permite ninguna otra
 * transicion.
 */
public class Cancelado implements EstadoTicket {

    private static final Cancelado INSTANCIA = new Cancelado();

    private Cancelado() {
    }

    public static Cancelado getInstancia() {
        return INSTANCIA;
    }

    @Override
    public String nombre() {
        return "CANCELADO";
    }
}
