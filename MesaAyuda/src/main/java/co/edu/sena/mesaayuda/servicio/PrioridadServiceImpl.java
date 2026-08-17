package co.edu.sena.mesaayuda.servicio;

import co.edu.sena.mesaayuda.modelo.Categoria;
import co.edu.sena.mesaayuda.modelo.Prioridad;
import co.edu.sena.mesaayuda.repositorio.PrioridadRepository;

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

/**
 * Determina la prioridad buscando palabras clave en titulo/descripcion. Si
 * ninguna palabra clave aparece, cae a MEDIA por defecto. Las palabras se
 * revisan sin tildes para que "caid" atrape "caido", "caída", "caidos", etc.
 */
public class PrioridadServiceImpl implements PrioridadService {

    // "caid" (sin terminacion) atrapa caido/caida/caidos/caidas, con o sin tilde
    // (el texto ya llega sin tildes gracias a quitarTildes).
    private static final String[] PALABRAS_CRITICAS = {"caid", "no funciona", "urgente", "sin servicio", "no enciende"};
    private static final String[] PALABRAS_ALTAS = {"no responde", "error", "falla", "bloqueado"};
    private static final String[] PALABRAS_BAJAS = {"solicitud", "consulta", "licencia", "instalar"};

    private final PrioridadRepository prioridadRepository;

    public PrioridadServiceImpl(PrioridadRepository prioridadRepository) {
        this.prioridadRepository = prioridadRepository;
    }

    @Override
    public Prioridad determinarPrioridad(String titulo, String descripcion, Categoria categoria) {
        String texto = quitarTildes((titulo + " " + descripcion).toLowerCase(Locale.forLanguageTag("es")));

        String nombrePrioridad = "MEDIA";
        if (contieneAlguna(texto, PALABRAS_CRITICAS)) {
            nombrePrioridad = "CRITICA";
        } else if (contieneAlguna(texto, PALABRAS_ALTAS)) {
            nombrePrioridad = "ALTA";
        } else if (contieneAlguna(texto, PALABRAS_BAJAS)) {
            nombrePrioridad = "BAJA";
        }

        List<Prioridad> prioridades = prioridadRepository.listarTodas();
        String nombreFinal = nombrePrioridad;
        return prioridades.stream()
                .filter(p -> p.getNombre().equalsIgnoreCase(nombreFinal))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("La prioridad " + nombreFinal + " no existe en la BD"));
    }

    private boolean contieneAlguna(String texto, String[] palabras) {
        for (String palabra : palabras) {
            if (texto.contains(palabra)) {
                return true;
            }
        }
        return false;
    }

    private String quitarTildes(String texto) {
        String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return normalizado.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
