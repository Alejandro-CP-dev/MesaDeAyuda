package co.edu.sena.mesaayuda.modelo.estado;

/**
 * Estado final: el solicitante confirmo la solucion.
 *
 * UNICA transicion permitida: reabrir (vuelve a EN_PROCESO). Esto es una
 * decision de negocio deliberada que va MAS ALLA de la tabla del taller
 * (seccion 7 del enunciado), que solo permite reabrir desde RESUELTO: en
 * este proyecto tambien se puede reabrir un ticket ya CERRADO, por si el
 * problema reaparece despues de que el solicitante lo dio por resuelto.
 * No admite ninguna otra transicion (ni siquiera cancelar: un ticket
 * cerrado ya no tiene sentido cancelarlo).
 */
public class Cerrado implements EstadoTicket {

    private static final Cerrado INSTANCIA = new Cerrado();

    private Cerrado() {
    }

    public static Cerrado getInstancia() {
        return INSTANCIA;
    }

    @Override
    public EstadoTicket reabrir() {
        return EnProceso.getInstancia();
    }

    @Override
    public String nombre() {
        return "CERRADO";
    }
}
