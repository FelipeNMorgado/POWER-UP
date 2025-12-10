package Up.Power.planoTreino;

import Up.Power.PlanoTreino;
import Up.Power.Treino;
import Up.Power.Email;
import Up.Power.EstadoPlano;

import java.util.List;

/**
 * Classe responsável pelas regras de negócio do plano de treino.
 */
public class PlanoTreinoService {

    private final PlanoTreinoRepository PlanoTreinoRepository;

    public PlanoTreinoService(PlanoTreinoRepository PlanoTreinoRepository) {
        this.PlanoTreinoRepository = PlanoTreinoRepository;
    }

    /**
     * Cria um novo plano de treino.
     */
    public PlanoTreino criarPlanoTreino(PlanoTId id, Email usuarioEmail, String nome) {
        if (usuarioEmail == null || nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Campos obrigatórios não podem ser nulos ou vazios");
        }
        
        // Se id for null, criar com ID 0 (será gerado pelo banco)
        PlanoTId planoTId = (id != null) ? id : new PlanoTId(0);
        PlanoTreino plano = new PlanoTreino(planoTId, usuarioEmail, nome);
        return plano;
    }

    /**
     * Salva um plano de treino no repositório.
     */
    public void salvarPlanoTreino(PlanoTreino plano) {
        if (plano == null) {
            throw new IllegalArgumentException("Plano não pode ser nulo");
        }
        
        // Permitir salvar plano vazio (exercícios podem ser adicionados depois)
        PlanoTreinoRepository.salvar(plano);
    }

    /**
     * Adiciona um treino a um plano existente.
     */
    public void adicionarTreino(PlanoTId planoTId, Treino treino) {
        if (planoTId == null || treino == null) {
            throw new IllegalArgumentException("ID do plano e treino não podem ser nulos");
        }
        
         List<PlanoTreino> planos = PlanoTreinoRepository.listar(planoTId);
        if (planos.isEmpty()) {
            throw new IllegalArgumentException("Plano de treino não encontrado");
        }
        
        PlanoTreino plano = planos.get(0);
        plano.adicionarTreino(treino);
        PlanoTreinoRepository.salvar(plano); // COMENTAR PARA TESTAR DEPENDÊNCIA DO SERVICE
    }

    /**
     * Remove um treino de um plano existente.
     */
    public void removerTreino(PlanoTId planoTId, Treino treino) {
        if (planoTId == null || treino == null) {
            throw new IllegalArgumentException("ID do plano e treino não podem ser nulos");
        }
        
        List<PlanoTreino> planos = PlanoTreinoRepository.listar(planoTId);
        if (planos.isEmpty()) {
            throw new IllegalArgumentException("Plano de treino não encontrado");
        }
        
        PlanoTreino plano = planos.get(0);
        plano.removerTreino(treino);
        PlanoTreinoRepository.salvar(plano); // COMENTAR PARA TESTAR DEPENDÊNCIA DO SERVICE
    }

    /**
     * Adiciona um dia a um plano existente.
     */
    public void adicionarDia(PlanoTId planoTId, Dias dia) {
        if (planoTId == null || dia == null) {
            throw new IllegalArgumentException("ID do plano e dia não podem ser nulos");
        }
        
        List<PlanoTreino> planos = PlanoTreinoRepository.listar(planoTId);
        if (planos.isEmpty()) {
            throw new IllegalArgumentException("Plano de treino não encontrado");
        }
        
        PlanoTreino plano = planos.get(0);
        plano.adicionarDia(dia);
        PlanoTreinoRepository.salvar(plano); // COMENTAR PARA TESTAR DEPENDÊNCIA DO SERVICE
    }

    /**
     * Atualiza todos os dias de um plano existente.
     */
    public void atualizarDias(PlanoTId planoTId, List<Dias> dias) {
        if (planoTId == null || dias == null) {
            throw new IllegalArgumentException("ID do plano e dias não podem ser nulos");
        }
        
        List<PlanoTreino> planos = PlanoTreinoRepository.listar(planoTId);
        if (planos.isEmpty()) {
            throw new IllegalArgumentException("Plano de treino não encontrado");
        }
        
        PlanoTreino plano = planos.get(0);
        plano.setDias(dias);
        PlanoTreinoRepository.salvar(plano);
    }

    /**
     * Atualiza um treino em um plano existente.
     */
    public void atualizarTreino(PlanoTId planoTId, Treino treinoAntigo, Treino treinoNovo) {
        if (planoTId == null || treinoAntigo == null || treinoNovo == null) {
            throw new IllegalArgumentException("Parâmetros não podem ser nulos");
        }
        
        List<PlanoTreino> planos = PlanoTreinoRepository.listar(planoTId);
        if (planos.isEmpty()) {
            throw new IllegalArgumentException("Plano de treino não encontrado");
        }
        
        PlanoTreino plano = planos.get(0);
        plano.atualizarTreino(treinoAntigo, treinoNovo);
        PlanoTreinoRepository.salvar(plano); // COMENTAR PARA TESTAR DEPENDÊNCIA DO SERVICE
    }

    /**
     * Altera o estado de um plano de treino.
     */
    public void alterarEstadoPlano(PlanoTId planoTId, EstadoPlano estado) {
        if (planoTId == null || estado == null) {
            throw new IllegalArgumentException("ID do plano e estado não podem ser nulos");
        }
        
        List<PlanoTreino> planos = PlanoTreinoRepository.listar(planoTId);
        if (planos.isEmpty()) {
            throw new IllegalArgumentException("Plano de treino não encontrado");
        }
        
        PlanoTreino plano = planos.get(0);
        plano.alterarEstadoPlano(estado);
        PlanoTreinoRepository.salvar(plano); // COMENTAR PARA TESTAR DEPENDÊNCIA DO SERVICE
    }

    /**
     * Obtém um plano de treino pelo ID.
     */
    public PlanoTreino obterPlanoTreino(PlanoTId planoTId) {
        if (planoTId == null) {
            throw new IllegalArgumentException("ID do plano não pode ser nulo");
        }
        
        List<PlanoTreino> planos = PlanoTreinoRepository.listar(planoTId);
        if (planos.isEmpty()) {
            throw new IllegalArgumentException("Plano de treino não encontrado");
        }
        
        return planos.get(0);
    }

    /**
     * Lista planos de treino.
     */
    public List<PlanoTreino> listarPlanosTreino(PlanoTId planoTId) {
         return PlanoTreinoRepository.listar(planoTId); // COMENTAR PARA TESTAR DEPENDÊNCIA DO SERVICE
//        return null; // DESCOMENTAR PARA TESTAR DEPENDÊNCIA DO SERVICE
    }

    /**
     * Exclui um plano de treino.
     */
    public void excluirPlanoTreino(PlanoTId planoTId) {
        if (planoTId == null) {
            throw new IllegalArgumentException("ID do plano não pode ser nulo");
        }
        
         PlanoTreinoRepository.excluir(planoTId); // COMENTAR PARA TESTAR DEPENDÊNCIA DO SERVICE
    }

}
