package Up.Power.conquista;

import Up.Power.Conquista;
import java.util.List;

public class ConquistaService {

    private final ConquistaRepository repository;
    private String ultimaMensagem;
    private String badgeAtual;

    public ConquistaService(ConquistaRepository repository) {
        this.repository = repository;
    }

    public void adicionarConquistaAtiva(Conquista conquista) {
        repository.salvar(conquista);
    }

    public boolean temConquistasAtivas() {
        return !repository.listarAtivas().isEmpty();
    }

    public boolean avaliarConquista(Conquista conquista, float desempenhoUsuario) {
        float requisito = 100f; // exemplo fixo — pode ser dinâmico depois

        if (desempenhoUsuario >= requisito) {
            repository.marcarComoConcluida(conquista);
            ultimaMensagem = "Recompensa enviada!";
            return true;
        } else {
            ultimaMensagem = "Conquista ainda não disponível.";
            return false;
        }
    }

    public boolean escolherBadge(Conquista conquista, String badge) {
        List<Conquista> concluidas = repository.listarConcluidas();

        if (!concluidas.contains(conquista)) {
            ultimaMensagem = "Você ainda não concluiu essa conquista.";
            return false;
        }

        badgeAtual = badge;
        ultimaMensagem = "Vantagem: +5% força";
        return true;
    }

    public List<Conquista> getConquistasConcluidas() {
        return repository.listarConcluidas();
    }

    public String getUltimaMensagem() {
        return ultimaMensagem;
    }

    public String getBadgeAtual() {
        return badgeAtual;
    }
}
