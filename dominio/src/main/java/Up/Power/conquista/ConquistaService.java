package Up.Power.conquista;

import Up.Power.Conquista;

import java.util.ArrayList;
import java.util.List;

public class ConquistaService {

    private List<Conquista> conquistasAtivas = new ArrayList<>();
    private List<Conquista> conquistasConcluidas = new ArrayList<>();
    private String ultimaMensagem;
    private String badgeAtual;

    public void adicionarConquistaAtiva(Conquista conquista) {
        conquistasAtivas.add(conquista);
    }

    public boolean temConquistasAtivas() {
        return !conquistasAtivas.isEmpty();
    }

    public boolean avaliarConquista(Conquista conquista, float desempenhoUsuario) {
        // Exemplo: conquista exige 100kg — compara com desempenho
        float requisito = 100f; // poderia vir da descrição ou atributo próprio

        if (desempenhoUsuario >= requisito) {
            conquistasConcluidas.add(conquista);
            ultimaMensagem = "Recompensa enviada!";
            return true;
        } else {
            ultimaMensagem = "Conquista ainda não disponível.";
            return false;
        }
    }

    public boolean escolherBadge(Conquista conquista, String badge) {
        if (!conquistasConcluidas.contains(conquista)) {
            ultimaMensagem = "Você ainda não concluiu essa conquista.";
            return false;
        }

        badgeAtual = badge;
        ultimaMensagem = "Vantagem: +5% força";
        return true;
    }

    public List<Conquista> getConquistasConcluidas() {
        return conquistasConcluidas;
    }

    public String getUltimaMensagem() {
        return ultimaMensagem;
    }

    public String getBadgeAtual() {
        return badgeAtual;
    }
}
