package co.edu.sena.mesaayuda.servicio;

import co.edu.sena.mesaayuda.modelo.Usuario;

/**
 * ISP: lo unico que un ADMINISTRADOR puede hacer sobre un ticket, ademas
 * de consultarlos todos (heredado de ConsultaTicketService).
 */
public interface OperacionesAdministrador extends ConsultaTicketService {

    /** RF-06: el admin cancela cualquier ticket no cerrado. */
    void cancelar(Long ticketId, Usuario administrador);

    /** RF-10: el admin reasigna el ticket a otro agente. */
    void reasignar(Long ticketId, Long nuevoAgenteId, Usuario administrador);
}
