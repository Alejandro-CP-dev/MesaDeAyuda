package co.edu.sena.mesaayuda.modelo.estado;

/**
 * Estado final: el solicitante confirmo la solucion. No admite ninguna
 * transicion (ni siquiera cancelar), por eso no sobreescribe ningun metodo:
 * hereda los defaults de EstadoTicket, que siempre lanzan
 * TransicionInvalidaException.
 */
public class Cerrado implements EstadoTicket {

    private static final Cerrado INSTANCIA = new Cerrado();

    private Cerrado() {
    }

    public static Cerrado getInstancia() {
        return INSTANCIA;
    }

    @Override
    public String nombre() {
        return "CERRADO";
    }
}
