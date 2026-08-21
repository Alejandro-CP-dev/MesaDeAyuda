package co.edu.sena.mesaayuda.servicio;

import co.edu.sena.mesaayuda.dto.TicketDTO;
import co.edu.sena.mesaayuda.modelo.Usuario;

/**
 * ISP: lo unico que un SOLICITANTE puede hacer sobre un ticket, ademas de
 * consultarlo (heredado de ConsultaTicketService). Ni iniciar/resolver
 * (eso es del agente) ni cancelar/reasignar (eso es del admin) aparecen
 * aqui: un Servlet que dependa de esta interfaz no puede, ni por error de
 * copiar y pegar, llamar una operacion que no le corresponde a este rol.
 */
public interface OperacionesSolicitante extends ConsultaTicketService {

    /** RF-02: registrar un ticket nuevo. */
    TicketDTO crearTicket(String titulo, String descripcion, Long categoriaId, Usuario solicitante);

    /** RF-06: el solicitante confirma la solucion. RESUELTO -> CERRADO. */
    void cerrar(Long ticketId, Usuario solicitante);

    /** RF-06: el solicitante indica que el problema persiste. RESUELTO -> EN_PROCESO. */
    void reabrir(Long ticketId, Usuario solicitante);
}
