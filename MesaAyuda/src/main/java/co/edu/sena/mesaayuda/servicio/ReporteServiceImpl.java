package co.edu.sena.mesaayuda.servicio;

import co.edu.sena.mesaayuda.dto.MetricaAgenteDTO;
import co.edu.sena.mesaayuda.dto.MetricaEstadoDTO;
import co.edu.sena.mesaayuda.dto.MetricasDTO;
import co.edu.sena.mesaayuda.modelo.Rol;
import co.edu.sena.mesaayuda.modelo.Ticket;
import co.edu.sena.mesaayuda.modelo.Usuario;
import co.edu.sena.mesaayuda.repositorio.TicketRepository;
import co.edu.sena.mesaayuda.repositorio.UsuarioRepository;
import co.edu.sena.mesaayuda.servicio.excepcion.AccesoNoAutorizadoException;

import java.util.ArrayList;
import java.util.List;

public class ReporteServiceImpl implements ReporteService {

    /** Orden fijo para que el reporte siempre muestre los 6 estados, incluso en 0. */
    private static final String[] ESTADOS = {
        "NUEVO", "ASIGNADO", "EN_PROCESO", "RESUELTO", "CERRADO", "CANCELADO"
    };

    private final TicketRepository ticketRepository;
    private final UsuarioRepository usuarioRepository;

    public ReporteServiceImpl(TicketRepository ticketRepository, UsuarioRepository usuarioRepository) {
        this.ticketRepository = ticketRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public MetricasDTO generarMetricas(Usuario administrador) {
        if (administrador.getRol() != Rol.ADMINISTRADOR) {
            throw new AccesoNoAutorizadoException("Solo un administrador puede consultar reportes");
        }

        List<Ticket> todos = ticketRepository.listarTodos();

        List<MetricaEstadoDTO> porEstado = new ArrayList<>();
        for (String estado : ESTADOS) {
            long cantidad = todos.stream().filter(t -> t.getEstado().nombre().equals(estado)).count();
            porEstado.add(new MetricaEstadoDTO(estado, cantidad));
        }

        long vencidos = todos.stream().filter(Ticket::vencido).count();

        List<Usuario> agentes = usuarioRepository.listarPorRol(Rol.AGENTE);
        List<MetricaAgenteDTO> porAgente = new ArrayList<>();
        for (Usuario agente : agentes) {
            List<Ticket> asignados = ticketRepository.listarPorAgente(agente.getId());
            long abiertos = asignados.stream()
                    .filter(t -> !"CERRADO".equals(t.getEstado().nombre()) && !"CANCELADO".equals(t.getEstado().nombre()))
                    .count();
            porAgente.add(new MetricaAgenteDTO(agente.getNombre(), asignados.size(), abiertos));
        }

        return new MetricasDTO(todos.size(), vencidos, porEstado, porAgente);
    }
}
