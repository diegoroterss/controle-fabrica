package fabrica;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final DateTimeFormatter FORMATO_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter FORMATO_COMPLETO = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private static final Scanner scanner = new Scanner(System.in);
    private static final RegistroDAO dao = new RegistroDAO();

    public static void main(String[] args) {
        try {
            Conexao.criarTabela();
        } catch (SQLException e) {
            System.out.println("Erro ao preparar o banco de dados: " + e.getMessage());
            return;
        }

        int opcao;
        do {
            System.out.println("\n=== CONTROLE DE PARÂMETROS ===");
            System.out.println("1 - Novo registro");
            System.out.println("2 - Listar todos os registros");
            System.out.println("3 - Listar registros reprovados");
            System.out.println("0 - Sair");
            opcao = lerInt("Opção: ");

            try {
                switch (opcao) {
                    case 1 -> novoRegistro();
                    case 2 -> listar(dao.listarTodos());
                    case 3 -> listar(dao.listarReprovados());
                    case 0 -> System.out.println("Encerrando...");
                    default -> System.out.println("Opção inválida.");
                }
            } catch (SQLException e) {
                System.out.println("Erro no banco de dados: " + e.getMessage());
            }
        } while (opcao != 0);

        scanner.close();
    }

    private static void novoRegistro() throws SQLException {
        int operador = lerInt("Operador (número): ");
        LocalDate data = lerData();
        LocalTime hora = lerHora();
        double temperatura = lerDouble("Temperatura (°C): ");
        double umidade = lerDouble("Umidade (%): ");

        System.out.print("Sugestão de melhoria (Enter para pular): ");
        String melhoria = scanner.nextLine().trim();

        Registro registro = new Registro(operador, LocalDateTime.of(data, hora),
                temperatura, umidade, melhoria);

        dao.salvar(registro);
        System.out.println("\nRegistro salvo!");
        exibir(registro);
    }

    private static void listar(List<Registro> registros) {
        if (registros.isEmpty()) {
            System.out.println("\nNenhum registro encontrado.");
            return;
        }
        registros.forEach(Main::exibir);
    }

    private static void exibir(Registro r) {
        System.out.println("\n--------------------------------");
        System.out.println("Operador: " + r.operador());
        System.out.println("Data/Hora: " + r.dataHora().format(FORMATO_COMPLETO));
        System.out.printf("Temperatura: %.2f°C%n", r.temperatura());
        System.out.printf("Umidade: %.2f%%%n", r.umidade());
        System.out.println(r.aprovado() ? "APROVADO" : "REPROVADO");

        if (!r.temperaturaOk()) {
            System.out.printf("  -> Temperatura fora do padrão (%.0f a %.0f°C)%n",
                    Registro.TEMPERATURA_MIN, Registro.TEMPERATURA_MAX);
        }
        if (!r.umidadeOk()) {
            System.out.printf("  -> Umidade fora do padrão (máx. %.0f%%)%n", Registro.UMIDADE_MAX);
        }
        if (r.melhoria() != null && !r.melhoria().isBlank()) {
            System.out.println("Sugestão de melhoria: " + r.melhoria());
        }
    }

    // ---------- Leitura segura de dados (repete até o usuário digitar certo) ----------

    private static int lerInt(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                return Integer.parseInt(scanner.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Digite um número inteiro válido.");
            }
        }
    }

    private static double lerDouble(String mensagem) {
        while (true) {
            System.out.print(mensagem);
            try {
                // aceita vírgula ou ponto: 21,5 ou 21.5
                return Double.parseDouble(scanner.nextLine().trim().replace(',', '.'));
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido (ex.: 21,5).");
            }
        }
    }

    private static LocalDate lerData() {
        while (true) {
            System.out.print("Data (dd/MM/aaaa) [Enter = hoje]: ");
            String texto = scanner.nextLine().trim();
            if (texto.isEmpty()) {
                return LocalDate.now();
            }
            try {
                return LocalDate.parse(texto, FORMATO_DATA);
            } catch (DateTimeParseException e) {
                System.out.println("Data inválida. Exemplo: 30/09/2026");
            }
        }
    }

    private static LocalTime lerHora() {
        while (true) {
            System.out.print("Hora (HH:mm) [Enter = agora]: ");
            String texto = scanner.nextLine().trim();
            if (texto.isEmpty()) {
                return LocalTime.now().withSecond(0).withNano(0);
            }
            try {
                return LocalTime.parse(texto, FORMATO_HORA);
            } catch (DateTimeParseException e) {
                System.out.println("Hora inválida. Exemplo: 14:30");
            }
        }
    }
}
