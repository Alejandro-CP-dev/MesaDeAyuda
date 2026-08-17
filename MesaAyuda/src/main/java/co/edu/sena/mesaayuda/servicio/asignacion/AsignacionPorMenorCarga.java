package co.edu.sena.mesaayuda.servicio.asignacion;

import co.edu.sena.mesaayuda.modelo.Ticket;
import co.edu.sena.mesaayuda.modelo.Usuario;
import co.edu.sena.mesaayuda.repositorio.TicketRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Asigna al agente con menos tickets abiertos (ASIGNADO o EN_PROCESO) en
 * este momento, para repartir la carga de trabajo de forma pareja.
 */
public class AsignacionPorMenorCarga implements EstrategiaAsignacion {

    private final TicketRepository ticketRepository;

    public AsignacionPorMenorCarga(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public Usuario elegirAgente(Ticket ticket, List<Usuario> agentes) {
        if (agentes.isEmpty()) {
            throw new IllegalStateException("No hay agentes disponibles para asignar el ticket");
        }
        List<Ticket> abiertos = ticketRepository.listarAbiertos();
        Map<Long, Long> cargaPorAgente = abiertos.stream()
                .filter(t -> t.getAgenteId() != null)
                .collect(Collectors.groupingBy(Ticket::getAgenteId, Collectors.counting()));

        return agentes.stream()
                .min(Comparator.comparingLong(a -> cargaPorAgente.getOrDefault(a.getId(), 0L)))
                .orElse(agentes.get(0));
    }
}
