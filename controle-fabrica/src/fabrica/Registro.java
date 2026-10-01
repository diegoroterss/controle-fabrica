package fabrica;

import java.time.LocalDateTime;

/**
 * Representa uma anotação de parâmetros feita por um operador.
 * As regras de validação (limites) ficam aqui, num único lugar.
 */
public record Registro(
        int operador,
        LocalDateTime dataHora,
        double temperatura,
        double umidade,
        String melhoria) {

    public static final double TEMPERATURA_MIN = 18;
    public static final double TEMPERATURA_MAX = 24;
    public static final double UMIDADE_MAX = 65;

    public boolean temperaturaOk() {
        return temperatura >= TEMPERATURA_MIN && temperatura <= TEMPERATURA_MAX;
    }

    public boolean umidadeOk() {
        return umidade <= UMIDADE_MAX;
    }

    public boolean aprovado() {
        return temperaturaOk() && umidadeOk();
    }
}
