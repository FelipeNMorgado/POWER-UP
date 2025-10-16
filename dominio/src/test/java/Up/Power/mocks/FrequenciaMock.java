package Up.Power.mocks;

import Up.Power.Frequencia;
import Up.Power.frequencia.FrequenciaId;
import Up.Power.frequencia.FrequenciaRepository;

import java.time.LocalDateTime;
import java.util.*;

public class FrequenciaMock implements FrequenciaRepository {

    private final Map<FrequenciaId, List<Frequencia>> banco;

    public FrequenciaMock(List<Frequencia> bancoEmMemoria) {
        this.banco = new HashMap<>();

        // Carrega lista inicial se houver
        if (bancoEmMemoria != null) {
            for (Frequencia f : bancoEmMemoria) {
                banco.computeIfAbsent(f.getId(), id -> new ArrayList<>()).add(f);
            }
        }
    }

    @Override
    public void salvar(Frequencia frequencia) {
        banco.computeIfAbsent(frequencia.getId(), id -> new ArrayList<>()).add(frequencia);
        System.out.println("Frequência salva: " + frequencia.getId());
    }

    @Override
    public Frequencia obterFrequencia(FrequenciaId id, LocalDateTime atual) {
        List<Frequencia> lista = banco.get(id);
        if (lista == null || lista.isEmpty()) return null;

        // Retorna a frequência mais recente até a data atual
        return lista.stream()
                .filter(f -> !f.getDataDePresenca().isAfter(atual))
                .max(Comparator.comparing(Frequencia::getDataDePresenca))
                .orElse(null);
    }

    // Extra útil para testes: retorna todas as frequências
    public List<Frequencia> listarTodas() {
        return banco.values().stream()
                .flatMap(Collection::stream)
                .toList();
    }

    // Extra útil: limpar o banco (caso precise em @After ou @Before nos testes)
    public void limpar() {
        banco.clear();
    }
}