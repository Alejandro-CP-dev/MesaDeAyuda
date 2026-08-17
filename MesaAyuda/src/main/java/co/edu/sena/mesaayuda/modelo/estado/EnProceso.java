package co.edu.sena.mesaayuda.modelo.estado;

/** El agente esta trabajando en el ticket. */
public class EnProceso implements EstadoTicket {

    private static final EnProceso INSTANCIA = new EnProceso();

    private EnProceso() {
    }

    public static EnProceso getInstancia() {
        return INSTANCIA;
    }

    @Override
    public EstadoTicket resolver() {
        return Resuelto.getInstancia();
    }

    @Override
    public EstadoTicket cancelar() {
        return Cancelado.getInstancia();
    }

    @Override
    public String nombre() {
        return "EN_PROCESO";
    }
}
