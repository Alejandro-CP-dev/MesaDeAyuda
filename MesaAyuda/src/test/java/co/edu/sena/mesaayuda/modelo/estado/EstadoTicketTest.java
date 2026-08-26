package co.edu.sena.mesaayuda.modelo.estado;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Prueba el patron State de forma aislada, sin base de datos: construye
 * cada estado y valida que solo permita las transiciones descritas en la
 * seccion 7 del enunciado (tabla de ciclo de vida del ticket).
 */
class EstadoTicketTest {

    @Test
    void nuevoSoloPuedeAsignarseOCancelarse() {
        EstadoTicket nuevo = Nuevo.getInstancia();

        assertEquals("ASIGNADO", nuevo.asignar().nombre());
        assertEquals("CANCELADO", nuevo.cancelar().nombre());
        assertThrows(TransicionInvalidaException.class, nuevo::iniciar);
        assertThrows(TransicionInvalidaException.class, nuevo::resolver);
        assertThrows(TransicionInvalidaException.class, nuevo::cerrar);
        assertThrows(TransicionInvalidaException.class, nuevo::reabrir);
    }

    @Test
    void asignadoSoloPuedeIniciarOCancelarse() {
        EstadoTicket asignado = Asignado.getInstancia();

        assertEquals("EN_PROCESO", asignado.iniciar().nombre());
        assertEquals("CANCELADO", asignado.cancelar().nombre());
        assertThrows(TransicionInvalidaException.class, asignado::asignar);
        assertThrows(TransicionInvalidaException.class, asignado::resolver);
    }

    @Test
    void enProcesoSoloPuedeResolverseOCancelarse() {
        EstadoTicket enProceso = EnProceso.getInstancia();

        assertEquals("RESUELTO", enProceso.resolver().nombre());
        assertEquals("CANCELADO", enProceso.cancelar().nombre());
        assertThrows(TransicionInvalidaException.class, enProceso::cerrar);
    }

    @Test
    void resueltoPuedeCerrarseReabrirseOCancelarse() {
        EstadoTicket resuelto = Resuelto.getInstancia();

        assertEquals("CERRADO", resuelto.cerrar().nombre());
        assertEquals("EN_PROCESO", resuelto.reabrir().nombre());
        assertEquals("CANCELADO", resuelto.cancelar().nombre());
    }

    @Test
    void cerradoSoloPuedeReabrirse() {
        EstadoTicket cerrado = Cerrado.getInstancia();

        // Extension deliberada sobre la tabla del enunciado (seccion 7):
        // un ticket CERRADO tambien se puede reabrir, por si el problema
        // reaparece despues de que el solicitante lo dio por resuelto.
        assertEquals("EN_PROCESO", cerrado.reabrir().nombre());

        assertThrows(TransicionInvalidaException.class, cerrado::asignar);
        assertThrows(TransicionInvalidaException.class, cerrado::iniciar);
        assertThrows(TransicionInvalidaException.class, cerrado::resolver);
        assertThrows(TransicionInvalidaException.class, cerrado::cerrar);
        assertThrows(TransicionInvalidaException.class, cerrado::cancelar);
    }

    @Test
    void cancelarUnTicketYaCerradoNoDeberiaSerPosible_reglaDelNegocio() {
        // Regla clave del taller: "cualquiera NO cerrado" puede cancelarse.
        // Un ticket CERRADO ya no admite cancelar.
        EstadoTicket cerrado = Cerrado.getInstancia();
        assertThrows(TransicionInvalidaException.class, cerrado::cancelar);
    }

    @Test
    void laFabricaReconstruyeElEstadoCorrectoDesdeElNombrePersistido() {
        assertEquals(Nuevo.getInstancia(), EstadoTicketFactory.desdeNombre("NUEVO"));
        assertEquals(EnProceso.getInstancia(), EstadoTicketFactory.desdeNombre("EN_PROCESO"));
        assertThrows(IllegalArgumentException.class, () -> EstadoTicketFactory.desdeNombre("NO_EXISTE"));
    }
}
