package co.edu.sena.mesaayuda.repositorio;

import co.edu.sena.mesaayuda.modelo.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaRepository {

    List<Categoria> listarTodas();

    Optional<Categoria> buscarPorId(Long id);
}
