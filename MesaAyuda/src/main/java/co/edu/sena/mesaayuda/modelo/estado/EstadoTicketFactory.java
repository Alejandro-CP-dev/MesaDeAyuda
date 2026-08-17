package co.edu.sena.mesaayuda.modelo.estado;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Traduce entre el nombre persistido en Ticket.Estado (String) y la
 * instancia EstadoTicket correspondiente. Es el UNICO lugar que conoce
 * todos los estados concretos a la vez (el mapper y los repositorios lo
 * usan para reconstruir un Ticket leido de la base de datos).
 */
public final class EstadoTicketFactory {

    private static final Map<String, Supplier<EstadoTicket>> ESTADOS = Map.of(
            "NUEVO", Nuevo::getInstancia,
            "ASIGNADO", Asignado::getInstancia,
            "EN_PROCESO", EnProceso::getInstancia,
            "RESUELTO", Resuelto::getInstancia,
            "CERRADO", Cerrado::getInstancia,
            "CANCELADO", Cancelado::getInstancia
    );

    private EstadoTicketFactory() {
    }

    public static EstadoTicket desdeNombre(String nombre) {
        Supplier<EstadoTicket> fabrica = ESTADOS.get(nombre);
        if (fabrica == null) {
            throw new IllegalArgumentException("Estado de ticket desconocido: " + nombre);
        }
        return fabrica.get();
    }
}
