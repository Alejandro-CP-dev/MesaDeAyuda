package co.edu.sena.mesaayuda.repositorio;

import co.edu.sena.mesaayuda.modelo.Rol;
import co.edu.sena.mesaayuda.modelo.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/** Implementacion JDBC de UsuarioRepository sobre MesaAyudaDb. */
public class UsuarioRepositoryJdbc implements UsuarioRepository {

    private static final String SELECT_BASE =
            "SELECT u.Id, u.Nombre, u.Correo, u.Password, r.Nombre AS RolNombre " +
            "FROM Usuario u JOIN Rol r ON r.Id = u.RolId ";

    @Override
    public Optional<Usuario> buscarPorId(Long id) {
        String sql = SELECT_BASE + "WHERE u.Id = ?";
        try (Connection conexion = ConexionBD.obtener();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setLong(1, id);
            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next() ? Optional.of(mapear(resultado)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error consultando el usuario " + id, e);
        }
    }

    @Override
    public Optional<Usuario> buscarPorCorreo(String correo) {
        String sql = SELECT_BASE + "WHERE u.Correo = ?";
        try (Connection conexion = ConexionBD.obtener();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, correo);
            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next() ? Optional.of(mapear(resultado)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error consultando el usuario con correo " + correo, e);
        }
    }

    @Override
    public List<Usuario> listarPorRol(Rol rol) {
        String sql = SELECT_BASE + "WHERE r.Nombre = ? ORDER BY u.Nombre";
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection conexion = ConexionBD.obtener();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setString(1, rol.name());
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    usuarios.add(mapear(resultado));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando usuarios con rol " + rol, e);
        }
        return usuarios;
    }

    private Usuario mapear(ResultSet resultado) throws SQLException {
        return new Usuario(
                resultado.getLong("Id"),
                resultado.getString("Nombre"),
                resultado.getString("Correo"),
                resultado.getString("Password"),
                Rol.valueOf(resultado.getString("RolNombre")));
    }
}
