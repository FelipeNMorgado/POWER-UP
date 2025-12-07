package Up.Power.aplicacao.meta;

import Up.Power.Meta;
import Up.Power.meta.MetaId;
import Up.Power.meta.RewardService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MetaServicoAplicacao {

    private final MetaRepositorioAplicacao metaRepositorioAplicacao;
    private final RewardService rewardService;

    public MetaServicoAplicacao(
            MetaRepositorioAplicacao metaRepositorioAplicacao,
            RewardService rewardService // RewardService é injetado diretamente, pois é um serviço de domínio/interface
    ) {
        this.metaRepositorioAplicacao = metaRepositorioAplicacao;
        this.rewardService = rewardService;
    }

    public MetaResumo obterPorId(Integer id) {
        return metaRepositorioAplicacao.obterPorId(new MetaId(id))
                .map(MetaResumoAssembler::toResumo)
                .orElse(null);
    }

    public List<MetaResumo> obterMetasPorUsuario(int userId) {
        return metaRepositorioAplicacao.obterPorUsuario(userId).stream()
                .map(MetaResumoAssembler::toResumo)
                .toList();
    }

    public boolean podeColetarRecompensas(Integer metaId) {
        Optional<Meta> metaOpt = metaRepositorioAplicacao.obterPorId(new MetaId(metaId));
        if (metaOpt.isEmpty()) {
            return false;
        }

        // Usa o serviço de domínio para verificar a regra de negócio
        return rewardService.canCollectRewards(metaOpt.get(), LocalDate.now());
    }

    // Nota: A lógica de criar/salvar Meta dependeria de um DTO de entrada (Command) específico.
    // Para manter a funcionalidade simples, implementamos apenas os métodos de busca e verificação de recompensa.
}