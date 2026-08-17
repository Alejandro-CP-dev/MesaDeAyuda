package co.edu.sena.mesaayuda.repositorio;

import co.edu.sena.mesaayuda.modelo.Rol;
import co.edu.sena.mesaayuda.modelo.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * DIP: los servicios dependen de esta interfaz, no de UsuarioRepositoryJdbc.
 * ISP: solo los metodos que el resto del sistema realmente necesita.
 */
public interface UsuarioRepository {

    Optional<Usuario> buscarPorId(Long id);

    Optional<Usuario> buscarPorCorreo(String correo);

    List<Usuario> listarPorRol(Rol rol);
}
