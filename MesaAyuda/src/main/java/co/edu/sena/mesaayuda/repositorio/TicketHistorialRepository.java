package co.edu.sena.mesaayuda.repositorio;

import co.edu.sena.mesaayuda.modelo.TicketHistorial;

import java.util.List;

public interface TicketHistorialRepository {

    void guardar(TicketHistorial historial);

    List<TicketHistorial> listarPorTicket(Long ticketId);
}
