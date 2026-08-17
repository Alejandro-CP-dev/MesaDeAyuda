package co.edu.sena.mesaayuda.repositorio;

import co.edu.sena.mesaayuda.modelo.Prioridad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PrioridadRepositoryJdbc implements PrioridadRepository {

    @Override
    public List<Prioridad> listarTodas() {
        String sql = "SELECT Id, Nombre, HorasSla FROM Prioridad ORDER BY HorasSla ASC";
        List<Prioridad> prioridades = new ArrayList<>();
        try (Connection conexion = ConexionBD.obtener();
             Statement sentencia = conexion.createStatement();
             ResultSet resultado = sentencia.executeQuery(sql)) {
            while (resultado.next()) {
                prioridades.add(mapear(resultado));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando prioridades", e);
        }
        return prioridades;
    }

    @Override
    public Optional<Prioridad> buscarPorId(Long id) {
        String sql = "SELECT Id, Nombre, HorasSla FROM Prioridad WHERE Id = ?";
        try (Connection conexion = ConexionBD.obtener();
             PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            sentencia.setLong(1, id);
            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next() ? Optional.of(mapear(resultado)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error consultando la prioridad " + id, e);
        }
    }

    private Prioridad mapear(ResultSet resultado) throws SQLException {
        return new Prioridad(resultado.getLong("Id"), resultado.getString("Nombre"), resultado.getInt("HorasSla"));
    }
}
