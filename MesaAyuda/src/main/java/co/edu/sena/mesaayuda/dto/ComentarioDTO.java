package co.edu.sena.mesaayuda.dto;

public class ComentarioDTO {

    private final String autorNombre;
    private final String texto;
    private final String fecha;

    public ComentarioDTO(String autorNombre, String texto, String fecha) {
        this.autorNombre = autorNombre;
        this.texto = texto;
        this.fecha = fecha;
    }

    public String getAutorNombre() {
        return autorNombre;
    }

    public String getTexto() {
        return texto;
    }

    public String getFecha() {
        return fecha;
    }
}
