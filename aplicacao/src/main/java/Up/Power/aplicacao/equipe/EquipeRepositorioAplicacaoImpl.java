package Up.Power.aplicacao.equipe;

import Up.Power.Equipe;
import Up.Power.Email;
import Up.Power.equipe.EquipeId;
import Up.Power.equipe.EquipeRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class EquipeRepositorioAplicacaoImpl implements EquipeRepositorioAplicacao {

    private final EquipeRepository equipeRepository;

    public EquipeRepositorioAplicacaoImpl(EquipeRepository equipeRepository) {
        this.equipeRepository = equipeRepository;
    }

    @Override
    public Optional<Equipe> obterPorId(EquipeId id) {
        List<Equipe> equipes = equipeRepository.listarEquipe(id, null);
        return equipes.isEmpty() ? Optional.empty() : Optional.of(equipes.get(0));
    }

    @Override
    public List<Equipe> listarPorUsuario(String usuarioEmail) {
        // Lista todas as equipes e filtra por membro
        List<Equipe> todasEquipes = equipeRepository.listarEquipe(null, null);
        Email email = new Email(usuarioEmail);
        return todasEquipes.stream()
                .filter(equipe -> equipe.isMembro(email))
                .toList();
    }

    @Override
    public Equipe salvar(Equipe equipe) {
        equipeRepository.salvar(equipe);
        return equipe;
    }

    @Override
    public void excluir(EquipeId id) {
        equipeRepository.excluir(id);
    }
}

