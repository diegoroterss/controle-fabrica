package fabrica;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/** Cuida da conexão com o banco e da criação da tabela. */
public final class Conexao {

    // Para trocar de banco, basta mudar esta URL (e o driver .jar).
    private static final String URL = "jdbc:sqlite:fabrica.db";

    private Conexao() {
    }

    public static Connection obter() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public static void criarTabela() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS registros (
                    id          INTEGER PRIMARY KEY AUTOINCREMENT,
                    operador    INTEGER NOT NULL,
                    data_hora   TEXT    NOT NULL,
                    temperatura REAL    NOT NULL,
                    umidade     REAL    NOT NULL,
                    aprovado    INTEGER NOT NULL,
                    melhoria    TEXT
                )
                """;

        try (Connection conn = obter(); Statement st = conn.createStatement()) {
            st.execute(sql);
        }
    }
}
