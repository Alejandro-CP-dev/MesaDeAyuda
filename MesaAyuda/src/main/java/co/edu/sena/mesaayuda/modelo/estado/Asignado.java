package co.edu.sena.mesaayuda.modelo.estado;

/** El ticket ya tiene agente. Espera a que el agente inicie la atencion. */
public class Asignado implements EstadoTicket {

    private static final Asignado INSTANCIA = new Asignado();

    private Asignado() {
    }

    public static Asignado getInstancia() {
        return INSTANCIA;
    }

    @Override
    public EstadoTicket iniciar() {
        return EnProceso.getInstancia();
    }

    @Override
    public EstadoTicket cancelar() {
        return Cancelado.getInstancia();
    }

    @Override
    public String nombre() {
        return "ASIGNADO";
    }
}
