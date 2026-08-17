package co.edu.sena.mesaayuda.servicio.sla;

import co.edu.sena.mesaayuda.modelo.Prioridad;

import java.time.LocalDateTime;

/**
 * Estrategia (Strategy) para calcular la fecha limite de atencion de un
 * ticket (RF-09). OCP: para cambiar la politica de SLA de la organizacion
 * (por ejemplo, no contar fines de semana) se crea otra clase que
 * implemente esta interfaz y se cablea en AppContextListener; TicketService
 * no cambia.
 */
public interface EstrategiaSla {

    LocalDateTime calcularFechaLimite(Prioridad prioridad, LocalDateTime fechaCreacion);
}
