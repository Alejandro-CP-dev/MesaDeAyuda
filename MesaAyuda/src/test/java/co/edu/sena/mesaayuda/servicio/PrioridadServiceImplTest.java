package co.edu.sena.mesaayuda.servicio;

import co.edu.sena.mesaayuda.modelo.Categoria;
import co.edu.sena.mesaayuda.modelo.Prioridad;
import co.edu.sena.mesaayuda.repositorio.PrioridadRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Prueba PrioridadServiceImpl (RF-03) con un PrioridadRepository de mentira
 * (List.of en memoria) para no depender de MySQL en las pruebas unitarias.
 */
class PrioridadServiceImplTest {

    private final PrioridadRepository prioridadesDePrueba = new PrioridadRepository() {
        private final List<Prioridad> datos = List.of(
                new Prioridad(1L, "BAJA", 48),
                new Prioridad(2L, "MEDIA", 24),
                new Prioridad(3L, "ALTA", 8),
                new Prioridad(4L, "CRITICA", 2));

        @Override
        public List<Prioridad> listarTodas() {
            return datos;
        }

        @Override
        public java.util.Optional<Prioridad> buscarPorId(Long id) {
            return datos.stream().filter(p -> p.getId().equals(id)).findFirst();
        }
    };

    private final PrioridadService servicio = new PrioridadServiceImpl(prioridadesDePrueba);
    private final Categoria categoriaRed = new Categoria(3L, "Red");

    @Test
    void unTicketConPalabraCaidoEsCritico() {
        Prioridad prioridad = servicio.determinarPrioridad("No hay internet", "La red esta caida en el aula 203", categoriaRed);
        assertEquals("CRITICA", prioridad.getNombre());
    }

    @Test
    void unTicketConPalabraErrorEsAlto() {
        Prioridad prioridad = servicio.determinarPrioridad("Error en el sistema", "Muestra un error al guardar", categoriaRed);
        assertEquals("ALTA", prioridad.getNombre());
    }

    @Test
    void unTicketSinPalabrasClaveEsMedioPorDefecto() {
        Prioridad prioridad = servicio.determinarPrioridad("Duda sobre el equipo", "Quisiera saber como configurar el monitor", categoriaRed);
        assertEquals("MEDIA", prioridad.getNombre());
    }
}
