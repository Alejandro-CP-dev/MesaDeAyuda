package co.edu.sena.mesaayuda.servicio;

import co.edu.sena.mesaayuda.dto.NotificacionDTO;
import co.edu.sena.mesaayuda.modelo.Notificacion;
import co.edu.sena.mesaayuda.modelo.Usuario;
import co.edu.sena.mesaayuda.repositorio.NotificacionRepository;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class NotificacionServiceImpl implements NotificacionService {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private final NotificacionRepository notificacionRepository;

    public NotificacionServiceImpl(NotificacionRepository notificacionRepository) {
        this.notificacionRepository = notificacionRepository;
    }

    @Override
    public List<NotificacionDTO> listarPropias(Usuario usuario) {
        List<Notificacion> notificaciones = notificacionRepository.listarPorDestinatario(usuario.getId());
        return notificaciones.stream()
                .map(n -> new NotificacionDTO(
                        n.getTicketId(),
                        n.getCanal().name(),
                        n.getAsunto(),
                        n.getMensaje(),
                        n.getFechaEnvio().format(FORMATO_FECHA)))
                .collect(Collectors.toList());
    }
}
