package co.edu.sena.mesaayuda.dto;

import java.util.List;

/**
 * Datos ya calculados para el dashboard de administrador (reto adicional:
 * "tickets por estado, por agente y SLA vencidos"). Como todo DTO, son
 * datos planos listos para pintar: el calculo vive en ReporteServiceImpl,
 * nunca en la JSP.
 */
public class MetricasDTO {

    private final long totalTickets;
    private final long ticketsVencidos;
    private final List<MetricaEstadoDTO> porEstado;
    private final List<MetricaAgenteDTO> porAgente;

    public MetricasDTO(long totalTickets, long ticketsVencidos,
                        List<MetricaEstadoDTO> porEstado, List<MetricaAgenteDTO> porAgente) {
        this.totalTickets = totalTickets;
        this.ticketsVencidos = ticketsVencidos;
        this.porEstado = porEstado;
        this.porAgente = porAgente;
    }

    public long getTotalTickets() {
        return totalTickets;
    }

    public long getTicketsVencidos() {
        return ticketsVencidos;
    }

    public List<MetricaEstadoDTO> getPorEstado() {
        return porEstado;
    }

    public List<MetricaAgenteDTO> getPorAgente() {
        return porAgente;
    }
}
