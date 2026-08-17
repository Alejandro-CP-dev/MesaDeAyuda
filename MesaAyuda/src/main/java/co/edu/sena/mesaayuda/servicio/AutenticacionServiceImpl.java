package co.edu.sena.mesaayuda.servicio;

import co.edu.sena.mesaayuda.modelo.Usuario;
import co.edu.sena.mesaayuda.repositorio.UsuarioRepository;
import co.edu.sena.mesaayuda.servicio.excepcion.CredencialesInvalidasException;

import java.util.Optional;

public class AutenticacionServiceImpl implements AutenticacionService {

    private final UsuarioRepository usuarioRepository;

    public AutenticacionServiceImpl(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Usuario autenticar(String correo, String password) {
        Optional<Usuario> usuario = usuarioRepository.buscarPorCorreo(correo);
        String hashIngresado = HashUtil.sha256(password);

        if (usuario.isEmpty() || !usuario.get().getPasswordHash().equalsIgnoreCase(hashIngresado)) {
            throw new CredencialesInvalidasException("Correo o contrasena incorrectos");
        }
        return usuario.get();
    }
}
