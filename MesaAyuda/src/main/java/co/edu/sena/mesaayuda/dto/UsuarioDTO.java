package co.edu.sena.mesaayuda.dto;

/** Datos de un usuario listos para pintar en la vista (sin el hash del password). */
public class UsuarioDTO {

    private final Long id;
    private final String nombre;
    private final String correo;
    private final String rol;

    public UsuarioDTO(Long id, String nombre, String correo, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
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

    public String getRol() {
        return rol;
    }
}
