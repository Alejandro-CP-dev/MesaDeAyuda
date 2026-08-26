package co.edu.sena.mesaayuda.servicio;

import co.edu.sena.mesaayuda.dto.MetricasDTO;
import co.edu.sena.mesaayuda.modelo.Usuario;

/**
 * Reto adicional: tablero de metricas (tickets por estado, por agente y
 * SLA vencidos). Es una operacion de solo consulta, exclusiva del rol
 * ADMINISTRADOR (por eso no forma parte de OperacionesAdministrador ni de
 * ConsultaTicketService: no es una operacion sobre UN ticket, es un
 * reporte agregado sobre TODOS).
 */
public interface ReporteService {

    MetricasDTO generarMetricas(Usuario administrador);
}
