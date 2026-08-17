package co.edu.sena.mesaayuda.servicio;

import co.edu.sena.mesaayuda.modelo.Usuario;

/** RF-07: agregar comentarios a un ticket (solicitante y agente). */
public interface ComentarioService {

    void agregarComentario(Long ticketId, Usuario autor, String texto);
}
