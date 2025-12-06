package Up.Power.aplicacao.planoTreino;

import Up.Power.Email;
import Up.Power.PlanoTreino;
import Up.Power.planoTreino.PlanoTId;
import Up.Power.planoTreino.PlanoTreinoRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PlanoTreinoRepositorioAplicacaoImpl implements PlanoTreinoRepositorioAplicacao {

    private final PlanoTreinoRepository planoTreinoRepository;

    public PlanoTreinoRepositorioAplicacaoImpl(PlanoTreinoRepository planoTreinoRepository) {
        this.planoTreinoRepository = planoTreinoRepository;
    }

    @Override
    public Optional<PlanoTreino> obterPorId(PlanoTId id) {
        List<PlanoTreino> planos = planoTreinoRepository.listar(id);
        return planos.isEmpty() ? Optional.empty() : Optional.of(planos.get(0));
    }

    @Override
    public List<PlanoTreino> listarPorUsuario(String usuarioEmail) {
        // Lista todos os planos e filtra por usuário
        List<PlanoTreino> todosPlanos = planoTreinoRepository.listar(null);
        Email email = new Email(usuarioEmail);
        return todosPlanos.stream()
                .filter(plano -> plano.getUsuarioEmail().equals(email))
                .toList();
    }

    @Override
    public PlanoTreino salvar(PlanoTreino plano) {
        planoTreinoRepository.salvar(plano);
        return plano;
    }

    @Override
    public void excluir(PlanoTId id) {
        planoTreinoRepository.excluir(id);
    }
}

