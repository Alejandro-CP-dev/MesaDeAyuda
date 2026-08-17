package co.edu.sena.mesaayuda.repositorio;

import co.edu.sena.mesaayuda.modelo.Categoria;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoriaRepositoryJdbc implements CategoriaRepository {

    @Override
    public List<Categoria> listarTodas() {
        String sql = "SELECT Id, Nombre FROM Categoria ORDER BY Nombre";
        List<Categoria> categorias = new ArrayList<>();
        try (Connection conexion = ConexionBD.obtener();
             Statement sentencia = conexion.createStatement();
             ResultSet resultado = sentencia.executeQuery(sql)) {
            while (resultado.next()) {
                categorias.add(new Categoria(resultado.getLong("Id"), resultado.getString("Nombre")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando categorias", e);
        }
        return categorias;
    }

    @Override
    public Optional<Categoria> buscarPorId(Long id) {
        String sql = "SELECT Id, Nombre FROM Categoria WHERE Id = ?";
        try (Connection conexion = ConexionBD.obtener();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setLong(1, id);
            try (ResultSet resultado = sentencia.executeQuery()) {
                if (resultado.next()) {
                    return Optional.of(new Categoria(resultado.getLong("Id"), resultado.getString("Nombre")));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error consultando la categoria " + id, e);
        }
    }
}
