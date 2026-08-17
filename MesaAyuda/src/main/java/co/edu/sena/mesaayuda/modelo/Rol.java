package co.edu.sena.mesaayuda.modelo;

/**
 * Roles del sistema (RF-01). Se guarda como enum porque el conjunto de
 * roles es cerrado y conocido: no es una variabilidad que deba resolverse
 * con Strategy, a diferencia del ciclo de vida del ticket.
 */
public enum Rol {
    SOLICITANTE,
    AGENTE,
    ADMINISTRADOR
}
