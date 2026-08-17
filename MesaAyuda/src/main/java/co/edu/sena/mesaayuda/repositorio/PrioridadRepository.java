package co.edu.sena.mesaayuda.repositorio;

import co.edu.sena.mesaayuda.modelo.Prioridad;

import java.util.List;
import java.util.Optional;

public interface PrioridadRepository {

    List<Prioridad> listarTodas();

    Optional<Prioridad> buscarPorId(Long id);
}
