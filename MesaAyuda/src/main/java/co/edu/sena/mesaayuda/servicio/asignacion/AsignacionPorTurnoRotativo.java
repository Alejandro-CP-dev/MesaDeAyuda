package co.edu.sena.mesaayuda.servicio.asignacion;

import co.edu.sena.mesaayuda.modelo.Ticket;
import co.edu.sena.mesaayuda.modelo.Usuario;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Reparte los tickets nuevos entre los agentes en orden ciclico (round
 * robin), sin mirar su carga actual. Mantiene un contador en memoria: al
 * ser un unico objeto compartido (se instancia una sola vez en
 * AppContextListener), el turno avanza de forma consistente entre
 * peticiones.
 */
public class AsignacionPorTurnoRotativo implements EstrategiaAsignacion {

    private final AtomicInteger siguienteIndice = new AtomicInteger(0);

    @Override
    public Usuario elegirAgente(Ticket ticket, List<Usuario> agentes) {
        if (agentes.isEmpty()) {
            throw new IllegalStateException("No hay agentes disponibles para asignar el ticket");
        }
        int indice = siguienteIndice.getAndIncrement() % agentes.size();
        return agentes.get(indice);
    }
}
