package co.edu.sena.mesaayuda.servicio.asignacion;

import co.edu.sena.mesaayuda.modelo.Ticket;
import co.edu.sena.mesaayuda.modelo.Usuario;

import java.util.List;

/**
 * Estrategia (Strategy) para elegir el agente que atendera un ticket nuevo
 * (RF-04). OCP: agregar una regla de asignacion nueva (por categoria, por
 * antiguedad, etc.) es crear una clase que implemente esta interfaz; no se
 * toca AsignacionService ni las demas estrategias.
 */
public interface EstrategiaAsignacion {

    /**
     * @param ticket  ticket que se va a asignar.
     * @param agentes agentes disponibles (rol AGENTE) entre los que elegir.
     * @return el agente elegido.
     */
    Usuario elegirAgente(Ticket ticket, List<Usuario> agentes);
}
