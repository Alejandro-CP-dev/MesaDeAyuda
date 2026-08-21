package co.edu.sena.mesaayuda.servicio;

import co.edu.sena.mesaayuda.modelo.Usuario;

/**
 * ISP: lo unico que un AGENTE puede hacer sobre un ticket asignado a el,
 * ademas de consultarlo (heredado de ConsultaTicketService).
 */
public interface OperacionesAgente extends ConsultaTicketService {

    /** RF-06: el agente inicia atencion. ASIGNADO -> EN_PROCESO. */
    void iniciarAtencion(Long ticketId, Usuario agente);

    /** RF-06: el agente resuelve. EN_PROCESO -> RESUELTO. */
    void resolver(Long ticketId, Usuario agente);
}
