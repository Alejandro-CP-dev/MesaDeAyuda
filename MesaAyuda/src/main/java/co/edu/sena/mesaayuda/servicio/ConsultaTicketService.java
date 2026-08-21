package co.edu.sena.mesaayuda.servicio;

import co.edu.sena.mesaayuda.dto.TicketDTO;
import co.edu.sena.mesaayuda.modelo.Usuario;

import java.util.List;

/**
 * Operaciones de SOLO LECTURA sobre tickets, comunes a los tres roles
 * (RF-05: el solicitante ve los suyos, el agente los asignados, el admin
 * todos). Es la unica parte que se comparte entre roles a proposito: leer
 * no es un privilegio de un rol especifico. Las operaciones de escritura
 * SI estan segregadas por rol en {@link OperacionesSolicitante},
 * {@link OperacionesAgente} y {@link OperacionesAdministrador}.
 */
public interface ConsultaTicketService {

    List<TicketDTO> listarParaUsuario(Usuario usuarioActual);

    TicketDTO obtenerDetalle(Long ticketId, Usuario usuarioActual);
}
