package co.edu.sena.mesaayuda.dto;

/** Una fila del reporte "tickets por agente". */
public class MetricaAgenteDTO {

    private final String nombreAgente;
    private final long cantidadAsignados;
    private final long cantidadAbiertos;

    public MetricaAgenteDTO(String nombreAgente, long cantidadAsignados, long cantidadAbiertos) {
        this.nombreAgente = nombreAgente;
        this.cantidadAsignados = cantidadAsignados;
        this.cantidadAbiertos = cantidadAbiertos;
    }

    public String getNombreAgente() {
        return nombreAgente;
    }

    public long getCantidadAsignados() {
        return cantidadAsignados;
    }

    public long getCantidadAbiertos() {
        return cantidadAbiertos;
    }
}
