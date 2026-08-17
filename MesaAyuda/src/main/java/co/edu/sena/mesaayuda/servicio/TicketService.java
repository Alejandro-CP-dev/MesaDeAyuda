package co.edu.sena.mesaayuda.servicio;

import co.edu.sena.mesaayuda.dto.TicketDTO;
import co.edu.sena.mesaayuda.modelo.Usuario;

import java.util.List;

/**
 * Logica de negocio de tickets (RF-02 a RF-10). Interfaz segregada (ISP):
 * solo expone las operaciones sobre tickets; autenticacion y comentarios
 * viven en sus propios servicios.
 */
public interface TicketService {

    TicketDTO crearTicket(String titulo, String descripcion, Long categoriaId, Usuario solicitante);

    void iniciarAtencion(Long ticketId, Usuario agente);

    void resolver(Long ticketId, Usuario agente);

    void cerrar(Long ticketId, Usuario solicitante);

    void reabrir(Long ticketId, Usuario solicitante);

    void cancelar(Long ticketId, Usuario administrador);

    void reasignar(Long ticketId, Long nuevoAgenteId, Usuario administrador);

    /** RF-05: el solicitante ve los suyos, el agente los asignados, el admin todos. */
    List<TicketDTO> listarParaUsuario(Usuario usuarioActual);

    TicketDTO obtenerDetalle(Long ticketId, Usuario usuarioActual);
}
