package co.edu.sena.mesaayuda.modelo.estado;

/**
 * Estado inicial de todo ticket. Unica transicion valida: que el sistema
 * asigne un agente (RF-04), o que el admin lo cancele.
 * Es un singleton sin estado interno (stateless): no hay razon para crear
 * una instancia nueva por cada ticket.
 */
public class Nuevo implements EstadoTicket {

    private static final Nuevo INSTANCIA = new Nuevo();

    private Nuevo() {
    }

    public static Nuevo getInstancia() {
        return INSTANCIA;
    }

    @Override
    public EstadoTicket asignar() {
        return Asignado.getInstancia();
    }

    @Override
    public EstadoTicket cancelar() {
        return Cancelado.getInstancia();
    }

    @Override
    public String nombre() {
        return "NUEVO";
    }
}
