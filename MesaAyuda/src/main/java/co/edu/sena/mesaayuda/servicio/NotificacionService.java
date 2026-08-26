package co.edu.sena.mesaayuda.servicio;

import co.edu.sena.mesaayuda.dto.NotificacionDTO;
import co.edu.sena.mesaayuda.modelo.Usuario;

import java.util.List;

/**
 * SRP: los Servlets no hablan directamente con NotificacionRepository
 * (regla del taller: "nada de SQL ni reglas en los Servlets"). Esta clase
 * es la unica que traduce "dame las notificaciones de este usuario" en una
 * consulta al repositorio.
 */
public interface NotificacionService {

    List<NotificacionDTO> listarPropias(Usuario usuario);
}
