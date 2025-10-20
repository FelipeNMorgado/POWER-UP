package Up.Power.mocks;

import Up.Power.Email;
import Up.Power.Feedback;
import Up.Power.feedback.FeedbackId;
import Up.Power.feedback.FeedbackRepository;
import Up.Power.frequencia.FrequenciaId;

import java.util.*;
import java.util.stream.Collectors;

public class FeedbackMock implements FeedbackRepository {

    private final Map<FeedbackId, Feedback> banco;

    public FeedbackMock(List<Feedback> bancoEmMemoria) {
        this.banco = new HashMap<>();

        if (bancoEmMemoria != null) {
            for (Feedback f : bancoEmMemoria) {
                banco.put(f.getId(), f);
            }
        }
    }

    @Override
    public void salvar(Feedback feedback) {
        banco.put(feedback.getId(), feedback);
        System.out.println("Feedback salvo/atualizado: " + feedback.getId());
    }

    @Override
    public void deletar(FeedbackId id) {
        Feedback removido = banco.remove(id);
        if (removido != null) {
            System.out.println("Feedback deletado: " + id);
        } else {
            System.out.println("Aviso: Tentativa de deletar Feedback inexistente: " + id);
        }
    }

    @Override
    public List<Feedback> listarFeedbacks(FeedbackId feedback, Email usuarioEmail) {
        if (usuarioEmail == null) {
            return new ArrayList<>(banco.values());
        }

        return banco.values().stream()
                .filter(f -> f.getEmail().equals(usuarioEmail))
                .collect(Collectors.toList());
    }

    @Override
    public Feedback obter(FeedbackId id) {
        // Simula a busca de um único item pelo ID.
        return banco.get(id);
    }

    @Override
    public void alterar(FeedbackId id) {
        if (banco.containsKey(id)) {
            System.out.println("Feedback encontrado para alteração (ID: " + id + ").");
        } else {
            System.out.println("Erro: Feedback não encontrado para alteração (ID: " + id + ").");
        }
    }

    // --- Métodos Extras Úteis para Testes ---

    public List<Feedback> listarTodas() {
        return new ArrayList<>(banco.values());
    }

    public void limpar() {
        banco.clear();
        System.out.println("Banco de Feedback Mock limpo.");
    }

    @Override
    public Feedback obterPorData(Date data) {
        for (Feedback f : banco.values()) {
            if (f.getData().equals(data)) {
                return f;
            }
        }
        return null;
    }

    @Override
    public Feedback obterPorFrequencia(FrequenciaId frequenciaId) {
        for (Feedback f : banco.values()) {
            if (f.getFrequencia().equals(frequenciaId)) {
                return f;
            }
        }
        return null;
    }


}