package co.edu.sena.mesaayuda.servicio;

import co.edu.sena.mesaayuda.modelo.Usuario;

/** RF-01: autenticacion de usuarios con rol. */
public interface AutenticacionService {

    /** @throws co.edu.sena.mesaayuda.servicio.excepcion.CredencialesInvalidasException si no coinciden. */
    Usuario autenticar(String correo, String password);
}
