package co.edu.sena.mesaayuda.repositorio;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utilidad para abrir conexiones JDBC contra MesaAyudaDb. Lee la
 * configuracion de db.properties (src/main/resources) para no dejar el
 * usuario/clave/URL quemados en el codigo.
 *
 * No es un pool de conexiones a proposito: para el alcance de este taller
 * (una conexion por peticion HTTP, cerrada con try-with-resources en cada
 * repositorio) es mas simple de explicar en la sustentacion que introducir
 * HikariCP u otro pool.
 */
public final class ConexionBD {

    private static final Properties PROPIEDADES = new Properties();

    static {
        try (InputStream entrada = ConexionBD.class.getClassLoader()
                .getResourceAsStream("db.properties")) {
            if (entrada == null) {
                throw new IllegalStateException("No se encontro db.properties en el classpath");
            }
            PROPIEDADES.load(entrada);
            Class.forName(PROPIEDADES.getProperty("db.driver"));
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("No se pudo inicializar la configuracion de base de datos", e);
        }
    }

    private ConexionBD() {
    }

    public static Connection obtener() throws SQLException {
        return DriverManager.getConnection(
                PROPIEDADES.getProperty("db.url"),
                PROPIEDADES.getProperty("db.usuario"),
                PROPIEDADES.getProperty("db.password"));
    }
}
