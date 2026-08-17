package co.edu.sena.mesaayuda.modelo;

import java.util.Objects;

/**
 * Usuario del sistema. El Password se guarda ya hasheado (SHA-256): esta
 * clase nunca maneja texto plano, ese trabajo es de AutenticacionService.
 */
public class Usuario {

    private final Long id;
    private final String nombre;
    private final String correo;
    private final String passwordHash;
    private final Rol rol;

    public Usuario(Long id, String nombre, String correo, String passwordHash, Rol rol) {
        this.id = id;
        this.nombre = Objects.requireNonNull(nombre, "El nombre es obligatorio");
        this.correo = Objects.requireNonNull(correo, "El correo es obligatorio");
        this.passwordHash = passwordHash;
        this.rol = Objects.requireNonNull(rol, "El rol es obligatorio");
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public Rol getRol() {
        return rol;
    }
}
