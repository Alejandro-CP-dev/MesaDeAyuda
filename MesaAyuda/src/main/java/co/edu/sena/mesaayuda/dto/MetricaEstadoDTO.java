package co.edu.sena.mesaayuda.dto;

/** Una fila del reporte "tickets por estado". */
public class MetricaEstadoDTO {

    private final String estado;
    private final long cantidad;

    public MetricaEstadoDTO(String estado, long cantidad) {
        this.estado = estado;
        this.cantidad = cantidad;
    }

    public String getEstado() {
        return estado;
    }

    public long getCantidad() {
        return cantidad;
    }
}
