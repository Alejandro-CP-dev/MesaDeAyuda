package co.edu.sena.mesaayuda.mapper;

import co.edu.sena.mesaayuda.dto.UsuarioDTO;
import co.edu.sena.mesaayuda.modelo.Usuario;

import java.util.List;
import java.util.stream.Collectors;

/** SRP: convierte Usuario (con su password hasheado) en UsuarioDTO (sin el). */
public final class UsuarioMapper {

    private UsuarioMapper() {
    }

    public static UsuarioDTO aDTO(Usuario usuario) {
        return new UsuarioDTO(usuario.getId(), usuario.getNombre(), usuario.getCorreo(), usuario.getRol().name());
    }

    public static List<UsuarioDTO> aDTO(List<Usuario> usuarios) {
        return usuarios.stream().map(UsuarioMapper::aDTO).collect(Collectors.toList());
    }
}
