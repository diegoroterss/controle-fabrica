package fabrica;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** DAO: única classe que conversa com o banco de dados. */
public class RegistroDAO {

    public void salvar(Registro r) throws SQLException {
        String sql = """
                INSERT INTO registros (operador, data_hora, temperatura, umidade, aprovado, melhoria)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.operador());
            ps.setString(2, r.dataHora().toString());
            ps.setDouble(3, r.temperatura());
            ps.setDouble(4, r.umidade());
            ps.setInt(5, r.aprovado() ? 1 : 0);
            ps.setString(6, r.melhoria());
            ps.executeUpdate();
        }
    }

    public List<Registro> listarTodos() throws SQLException {
        return buscar("SELECT * FROM registros ORDER BY data_hora DESC");
    }

    public List<Registro> listarReprovados() throws SQLException {
        return buscar("SELECT * FROM registros WHERE aprovado = 0 ORDER BY data_hora DESC");
    }

    private List<Registro> buscar(String sql) throws SQLException {
        List<Registro> lista = new ArrayList<>();

        try (Connection conn = Conexao.obter();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new Registro(
                        rs.getInt("operador"),
                        LocalDateTime.parse(rs.getString("data_hora")),
                        rs.getDouble("temperatura"),
                        rs.getDouble("umidade"),
                        rs.getString("melhoria")));
            }
        }
        return lista;
    }
}
