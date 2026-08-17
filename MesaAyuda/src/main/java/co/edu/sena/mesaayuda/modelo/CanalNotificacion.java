package co.edu.sena.mesaayuda.modelo;

/**
 * Canales de notificacion soportados. El valor Canal en la tabla
 * Notificacion es VARCHAR (no una tabla catalogo): agregar un canal nuevo
 * es crear una clase que implemente Notificador, sin tocar el esquema
 * (ver servicio.notificacion, OCP).
 */
public enum CanalNotificacion {
    CORREO,
    SMS,
    APLICACION
}
