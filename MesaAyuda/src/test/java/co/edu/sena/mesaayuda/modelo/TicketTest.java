package co.edu.sena.mesaayuda.modelo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Prueba el flujo del codigo OTP para cerrar un ticket (reto adicional),
 * de forma aislada, sin base de datos: construye un Ticket, lo lleva a
 * RESUELTO, le asigna un codigo a mano (como hace TicketServiceImpl) y
 * verifica que cerrar() solo funcione con el codigo correcto.
 */
class TicketTest {

    private Ticket ticketResuelto(String codigo) {
        Categoria categoria = new Categoria(1L, "Red");
        Prioridad prioridad = new Prioridad(1L, "ALTA", 8);
        Ticket ticket = new Ticket("No hay internet", "Sala 203 sin conexion", categoria, prioridad, 10L);
        ticket.asignar(20L);
        ticket.iniciar();
        ticket.resolver();
        ticket.definirCodigoCierre(codigo);
        return ticket;
    }

    @Test
    void cerrarConElCodigoCorrectoFunciona() {
        Ticket ticket = ticketResuelto("123456");

        ticket.cerrar("123456");

        assertEquals("CERRADO", ticket.getEstado().nombre());
    }

    @Test
    void cerrarConElCodigoIncorrectoLanzaExcepcionYNoCambiaElEstado() {
        Ticket ticket = ticketResuelto("123456");

        assertThrows(CodigoCierreInvalidoException.class, () -> ticket.cerrar("000000"));
        // El estado no debe haber cambiado: la validacion del codigo pasa
        // ANTES de tocar el estado.
        assertEquals("RESUELTO", ticket.getEstado().nombre());
    }

    @Test
    void elCodigoNoSePuedeReutilizarDespuesDeCerrar() {
        Ticket ticket = ticketResuelto("123456");
        ticket.cerrar("123456");

        // El ticket ya esta CERRADO; reabrir() vuelve a EN_PROCESO y el
        // codigo anterior queda invalidado (definirCodigoCierre(null) en
        // reabrir()). Sin un codigo nuevo, cerrar otra vez debe fallar
        // incluso repitiendo el mismo numero.
        ticket.reabrir();
        ticket.resolver();

        assertThrows(CodigoCierreInvalidoException.class, () -> ticket.cerrar("123456"));
    }

    @Test
    void sinCodigoAsignadoCerrarSiempreFalla() {
        Categoria categoria = new Categoria(1L, "Software");
        Prioridad prioridad = new Prioridad(2L, "MEDIA", 24);
        Ticket ticket = new Ticket("Instalar Office", "Necesito la licencia", categoria, prioridad, 10L);
        ticket.asignar(20L);
        ticket.iniciar();
        ticket.resolver();
        // A proposito NO se llama definirCodigoCierre(): simula el caso en
        // que TicketServiceImpl tuviera un error y nunca generara el codigo.

        assertThrows(CodigoCierreInvalidoException.class, () -> ticket.cerrar("123456"));
    }
}
