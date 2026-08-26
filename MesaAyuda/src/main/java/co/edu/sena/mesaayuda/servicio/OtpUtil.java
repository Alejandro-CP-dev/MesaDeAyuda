package co.edu.sena.mesaayuda.servicio;

import java.security.SecureRandom;

/**
 * Genera codigos OTP de 6 digitos para confirmar el cierre de un ticket
 * (reto adicional). SecureRandom en vez de Random: aunque el codigo es de
 * corta duracion, no cuesta nada usar el generador criptografico.
 */
public final class OtpUtil {

    private static final SecureRandom ALEATORIO = new SecureRandom();

    private OtpUtil() {
    }

    /** @return un codigo de 6 digitos, con ceros a la izquierda si hace falta (ej. "004821"). */
    public static String generarCodigo() {
        int numero = ALEATORIO.nextInt(1_000_000);
        return String.format("%06d", numero);
    }
}
