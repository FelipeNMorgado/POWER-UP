package Up.Power.equipe;

import Up.Power.Email;
import Up.Power.Equipe;
import Up.Power.equipe.EquipeId;
import Up.Power.equipe.EquipeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class EquipeService {
    
    private final EquipeRepository equipeRepository;
    
    public EquipeService(EquipeRepository equipeRepository) {
        this.equipeRepository = equipeRepository;
    }
    
    public Equipe criarEquipe(EquipeId id, String nome, Email usuarioAdm) {
        Equipe equipe = new Equipe(id, nome, usuarioAdm);
        equipeRepository.salvar(equipe); // COMENTAR PARA TESTAR DEPENDÊNCIA DO SERVICE
        return equipe;
    }
    
    public void adicionarMembro(EquipeId equipeId, Email novoMembro) {
        Equipe equipe = obterEquipe(equipeId);
        if (equipe == null) {
            throw new IllegalArgumentException("Equipe não encontrada: " + equipeId);
        }
        
        equipe.adicionarMembro(novoMembro);
        equipeRepository.salvar(equipe); // COMENTAR PARA TESTAR DEPENDÊNCIA DO SERVICE
    }
    
    public void removerMembro(EquipeId equipeId, Email membro) {
        Equipe equipe = obterEquipe(equipeId);
        if (equipe == null) {
            throw new IllegalArgumentException("Equipe não encontrada: " + equipeId);
        }
        
        equipe.removerMembro(membro);
        equipeRepository.salvar(equipe); // COMENTAR PARA TESTAR DEPENDÊNCIA DO SERVICE
    }
    
    public void atualizarInformacoes(EquipeId equipeId, String nome, String descricao, String foto) {
        Equipe equipe = obterEquipe(equipeId);
        if (equipe == null) {
            throw new IllegalArgumentException("Equipe não encontrada: " + equipeId);
        }
        
        equipe.atualizarInformacoes(nome, descricao, foto);
        equipeRepository.salvar(equipe); // COMENTAR PARA TESTAR DEPENDÊNCIA DO SERVICE
    }
    
    public void definirPeriodo(EquipeId equipeId, LocalDate inicio, LocalDate fim) {
        Equipe equipe = obterEquipe(equipeId);
        if (equipe == null) {
            throw new IllegalArgumentException("Equipe não encontrada: " + equipeId);
        }
        
        // Validação de negócio: data fim deve ser posterior à data início
        if (fim.isBefore(inicio)) {
            throw new IllegalArgumentException("Data fim não pode ser anterior à data início");
        }
        
        equipe.definirPeriodo(inicio, fim);
        equipeRepository.salvar(equipe); // COMENTAR PARA TESTAR DEPENDÊNCIA DO SERVICE
    }
    
    public Equipe obterEquipe(EquipeId equipeId) {
        List<Equipe> equipes = equipeRepository.listarEquipe(equipeId, null);
        return equipes.isEmpty() ? null : equipes.get(0);
    }
    
    public List<Equipe> listarEquipes(EquipeId equipeId) {
        return equipeRepository.listarEquipe(equipeId, null);
    }
    
    public boolean isLider(EquipeId equipeId, Email usuario) {
        Equipe equipe = obterEquipe(equipeId);
        return equipe != null && equipe.isLider(usuario);
    }
    
    public boolean isMembro(EquipeId equipeId, Email usuario) {
        Equipe equipe = obterEquipe(equipeId);
        return equipe != null && equipe.isMembro(usuario);
    }
}
