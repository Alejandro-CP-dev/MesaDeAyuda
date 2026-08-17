package co.edu.sena.mesaayuda.modelo.estado;

/**
 * El agente marco el ticket como resuelto. El solicitante decide si lo
 * cierra (queda conforme) o lo reabre (el problema persiste).
 */
public class Resuelto implements EstadoTicket {

    private static final Resuelto INSTANCIA = new Resuelto();

    private Resuelto() {
    }

    public static Resuelto getInstancia() {
        return INSTANCIA;
    }

    @Override
    public EstadoTicket cerrar() {
        return Cerrado.getInstancia();
    }

    @Override
    public EstadoTicket reabrir() {
        return EnProceso.getInstancia();
    }

    @Override
    public EstadoTicket cancelar() {
        return Cancelado.getInstancia();
    }

    @Override
    public String nombre() {
        return "RESUELTO";
    }
}
