package co.edu.sena.mesaayuda.servicio;

import co.edu.sena.mesaayuda.modelo.Categoria;
import co.edu.sena.mesaayuda.modelo.Prioridad;

/** RF-03: asigna automaticamente una prioridad segun la categoria o palabras clave. */
public interface PrioridadService {

    Prioridad determinarPrioridad(String titulo, String descripcion, Categoria categoria);
}
