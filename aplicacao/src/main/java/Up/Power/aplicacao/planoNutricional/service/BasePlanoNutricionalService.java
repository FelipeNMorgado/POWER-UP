package Up.Power.aplicacao.planoNutricional.service;

import Up.Power.PlanoNutricional;
import Up.Power.aplicacao.planoNutricional.commands.CriarPlanoNutricionalCommand;
import Up.Power.aplicacao.planoNutricional.commands.ModificarPlanoNutricionalCommand;
import Up.Power.planoNutricional.PlanoNId;
import Up.Power.planoNutricional.PlanoNutricionalRepository;
import Up.Power.refeicao.RefeicaoId;
import Up.Power.Email;
import org.springframework.stereotype.Service;

@Service("basePlanoNutricionalService")
public class BasePlanoNutricionalService implements PlanoNutricionalApplicationService {

    private final PlanoNutricionalRepository repository;

    public BasePlanoNutricionalService(PlanoNutricionalRepository repository) {
        this.repository = repository;
    }

    @Override
    public PlanoNutricional criar(CriarPlanoNutricionalCommand command) {
        System.out.println("[BASE_SERVICE] ========================================");
        System.out.println("[BASE_SERVICE] Iniciando criação de plano nutricional");
        System.out.println("[BASE_SERVICE] Command: objetivo=" + command.objetivo() + 
                ", email=" + command.usuarioEmail() + 
                ", caloriasObjetivo=" + command.caloriasObjetivo() + 
                ", refeicoesIds=" + (command.refeicoesIds() != null ? command.refeicoesIds().size() + " itens" : "null"));
        try {
            System.out.println("[BASE_SERVICE] Criando objeto PlanoNutricional...");
            PlanoNutricional plano =
                    new PlanoNutricional(
                            new PlanoNId(0),
                            command.objetivo(),
                            new Email(command.usuarioEmail())
                    );
            System.out.println("[BASE_SERVICE] PlanoNutricional criado. ID inicial: " + 
                    (plano.getId() != null ? plano.getId().getId() : "null"));

            // IMPORTANTE: Ao criar um novo plano, NUNCA adicionar refeições existentes
            // Um novo plano deve sempre começar vazio
            if (command.refeicoesIds() != null && !command.refeicoesIds().isEmpty()) {
                System.err.println("[BASE_SERVICE] AVISO: Tentativa de criar plano com refeições! Isso não deve acontecer.");
                System.err.println("[BASE_SERVICE] Refeições recebidas: " + command.refeicoesIds());
                System.err.println("[BASE_SERVICE] IGNORANDO refeições e criando plano vazio.");
                // NÃO adicionar refeições - plano novo deve estar vazio
            } else {
                System.out.println("[BASE_SERVICE] Nenhuma refeição para adicionar (lista vazia ou null) - CORRETO para novo plano");
            }
            
            // Garantir que o plano está completamente vazio
            plano.limparRefeicoes();
            System.out.println("[BASE_SERVICE] Plano limpo. Refeições após limpeza: " + 
                    (plano.getRefeicoes() != null ? plano.getRefeicoes().size() : "null"));
            
            // Verificação final: garantir que o plano está realmente vazio
            if (plano.getRefeicoes() != null && !plano.getRefeicoes().isEmpty()) {
                System.err.println("[BASE_SERVICE] ERRO CRÍTICO: Plano ainda tem refeições após limpeza! Forçando limpeza...");
                plano.getRefeicoes().clear();
            }

            // Se o usuário forneceu uma meta de calorias, usar ela
            if (command.caloriasObjetivo() != null && command.caloriasObjetivo() > 0) {
                System.out.println("[BASE_SERVICE] Definindo meta de calorias: " + command.caloriasObjetivo());
                plano.definirCaloriasObjetivo(command.caloriasObjetivo());
                System.out.println("[BASE_SERVICE] Meta de calorias definida com sucesso");
            } else {
                System.out.println("[BASE_SERVICE] Meta de calorias não fornecida ou inválida (será calculada pelo decorator)");
            }

            System.out.println("[BASE_SERVICE] Chamando repository.salvar...");
            repository.salvar(plano);
            System.out.println("[BASE_SERVICE] Repository.salvar concluído. ID após salvar: " + 
                    (plano.getId() != null ? plano.getId().getId() : "null"));
            System.out.println("[BASE_SERVICE] ========================================");
            return plano;
        } catch (Exception e) {
            System.err.println("[BASE_SERVICE] ========================================");
            System.err.println("[BASE_SERVICE] ERRO em BasePlanoNutricionalService.criar:");
            System.err.println("[BASE_SERVICE] Tipo: " + e.getClass().getName());
            System.err.println("[BASE_SERVICE] Mensagem: " + (e.getMessage() != null ? e.getMessage() : "null"));
            System.err.println("[BASE_SERVICE] Causa: " + (e.getCause() != null ? e.getCause().getClass().getName() + " - " + e.getCause().getMessage() : "null"));
            System.err.println("[BASE_SERVICE] Stack trace:");
            e.printStackTrace();
            System.err.println("[BASE_SERVICE] ========================================");
            throw e;
        }
    }

    @Override
    public PlanoNutricional modificar(ModificarPlanoNutricionalCommand command) {
        PlanoNutricional plano = repository.obter(new PlanoNId(command.planoId()));
        
        if (plano == null) {
            throw new IllegalArgumentException("Plano não encontrado");
        }

        // Criar novo plano com os dados atualizados, mantendo o email existente
        Email emailUsuario = plano.getUsuarioEmail() != null 
            ? plano.getUsuarioEmail() 
            : (command.usuarioEmail() != null ? new Email(command.usuarioEmail()) : null);
        
        if (emailUsuario == null) {
            throw new IllegalArgumentException("Email do usuário é obrigatório");
        }

        PlanoNutricional planoAtualizado = new PlanoNutricional(
                plano.getId(),
                command.objetivo(),
                emailUsuario
        );

        // IMPORTANTE: Manter as refeições existentes do plano original
        // O comando pode conter todas as refeições (existentes + novas) ou apenas as novas
        // Vamos usar as refeições do comando se fornecidas, caso contrário manter as existentes
        if (command.refeicoesIds() != null && !command.refeicoesIds().isEmpty()) {
            System.out.println("[BASE_SERVICE] Modificando plano. Refeições fornecidas no comando: " + command.refeicoesIds().size());
            System.out.println("[BASE_SERVICE] Refeições existentes no plano: " + (plano.getRefeicoes() != null ? plano.getRefeicoes().size() : 0));
            
            // Substituir todas as refeições pelas fornecidas no comando
            // (o frontend envia todas as refeições: existentes + novas)
            planoAtualizado.adicionarRefeicaoList(
                    command.refeicoesIds()
                            .stream()
                            .map(RefeicaoId::new)
                            .toList()
            );
            System.out.println("[BASE_SERVICE] Refeições atualizadas no plano: " + planoAtualizado.getRefeicoes().size());
        } else {
            // Se não foram fornecidas refeições no comando, manter as existentes
            System.out.println("[BASE_SERVICE] Nenhuma refeição fornecida no comando. Mantendo refeições existentes.");
            if (plano.getRefeicoes() != null && !plano.getRefeicoes().isEmpty()) {
                planoAtualizado.adicionarRefeicaoList(plano.getRefeicoes());
            }
        }

        // Manter calorias do plano original se já existirem
        planoAtualizado.definirCaloriasTotais(plano.getCaloriasTotais());
        // Manter caloriasObjetivo do plano original (o comando não inclui esse campo)
        planoAtualizado.definirCaloriasObjetivo(plano.getCaloriasObjetivo());

        repository.salvar(planoAtualizado);
        return planoAtualizado;
    }

    public java.util.List<PlanoNutricional> listarPorUsuario(String usuarioEmail) {
        return repository.listarPorUsuario(usuarioEmail);
    }

    @Override
    public void excluir(int planoId) {
        System.out.println("[BASE_SERVICE] Excluindo plano nutricional ID: " + planoId);
        repository.excluir(new PlanoNId(planoId));
        System.out.println("[BASE_SERVICE] Plano nutricional excluído com sucesso");
    }
}