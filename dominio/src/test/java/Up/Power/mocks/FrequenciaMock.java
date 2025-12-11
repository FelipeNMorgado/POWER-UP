package Up.Power.mocks;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import Up.Power.Frequencia;
import Up.Power.frequencia.FrequenciaId;
import Up.Power.frequencia.FrequenciaRepository;

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

    @Override
    public List<Frequencia> listarPorPerfil(Integer perfilId) {
        return banco.values().stream()
                .flatMap(List::stream)
                .filter(f -> f.getPerfil().getId() == (int) perfilId)
                .toList();
    }

    @Override
    public List<Frequencia> listarPorPerfilEPlanoTreino(Integer perfilId, Integer planoTreinoId) {
        return banco.values().stream()
                .flatMap(List::stream)
                .filter(f -> f.getPerfil().getId() == (int) perfilId)
                .filter(f -> f.getPlanoTId() != null && f.getPlanoTId().getId() == (int) planoTreinoId)
                .toList();
    }

    @Override
    public List<Frequencia> listarPorPerfilEData(Integer perfilId, java.time.LocalDate data) {
        return banco.values().stream()
                .flatMap(List::stream)
                .filter(f -> f.getPerfil().getId() == (int) perfilId)
                .filter(f -> f.getDataDePresenca().toLocalDate().equals(data))
                .toList();
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