package co.edu.sena.mesaayuda.repositorio;

import co.edu.sena.mesaayuda.modelo.Ticket;

import java.util.List;
import java.util.Optional;

/**
 * DIP: TicketService depende de esta interfaz. Hoy hay una unica
 * implementacion JDBC; para pruebas unitarias bastaria con crear un
 * TicketRepositoryEnMemoria que la implemente, igual que en el proyecto de
 * referencia.
 */
public interface TicketRepository {

    Ticket guardar(Ticket ticket);

    void actualizar(Ticket ticket);

    Optional<Ticket> buscarPorId(Long id);

    List<Ticket> listarTodos();

    List<Ticket> listarPorSolicitante(Long solicitanteId);

    List<Ticket> listarPorAgente(Long agenteId);

    /** Tickets en un estado de trabajo activo (no CERRADO ni CANCELADO). Los usa la estrategia de menor carga. */
    List<Ticket> listarAbiertos();
}
