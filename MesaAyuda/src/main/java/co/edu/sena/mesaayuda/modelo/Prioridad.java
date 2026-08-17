package co.edu.sena.mesaayuda.modelo;

/**
 * Prioridad de un ticket. Cada prioridad trae su propio SLA en horas; esa
 * regla la lee la estrategia de calculo de SLA (servicio.sla), no esta
 * clase: aqui solo se representa el dato.
 */
public class Prioridad {

    private final Long id;
    private final String nombre;
    private final int horasSla;

    public Prioridad(Long id, String nombre, int horasSla) {
        this.id = id;
        this.nombre = nombre;
        this.horasSla = horasSla;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getHorasSla() {
        return horasSla;
    }
}
