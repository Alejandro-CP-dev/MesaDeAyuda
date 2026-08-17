package co.edu.sena.mesaayuda.servicio.sla;

import co.edu.sena.mesaayuda.modelo.Prioridad;

import java.time.LocalDateTime;

/**
 * Implementacion estandar: la fecha limite es la fecha de creacion mas las
 * horas de SLA configuradas en la propia Prioridad (tabla Prioridad.HorasSla).
 */
public class SlaPorHorasPrioridad implements EstrategiaSla {

    @Override
    public LocalDateTime calcularFechaLimite(Prioridad prioridad, LocalDateTime fechaCreacion) {
        return fechaCreacion.plusHours(prioridad.getHorasSla());
    }
}
