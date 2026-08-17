package co.edu.sena.mesaayuda.repositorio;

import co.edu.sena.mesaayuda.modelo.Comentario;

import java.util.List;

public interface ComentarioRepository {

    Comentario guardar(Comentario comentario);

    List<Comentario> listarPorTicket(Long ticketId);
}
