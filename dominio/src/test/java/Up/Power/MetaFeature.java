package Up.Power;

import Up.Power.meta.MetaRepository;
import Up.Power.meta.RewardService;
import Up.Power.mocks.MetaMock;
import Up.Power.mocks.RewardServiceMock;
import Up.Power.meta.MetaId;
import Up.Power.exercicio.ExercicioId;
import Up.Power.treino.TreinoId;

import io.cucumber.java.pt.*;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

public class MetaFeature {

    private MetaRepository metaRepository;
    private RewardService rewardService;

    private Meta metaCriada;
    private Boolean podeColetar;

    // ---------- Cenário 1 ----------

    @Dado("que o usuário ainda não tenha metas")
    public void usuario_sem_metas() {
        metaRepository = new MetaMock();
        rewardService  = new RewardServiceMock();

        // garante que está vazio
        metaRepository.deleteAll();
        assertTrue(((MetaMock) metaRepository).findAll().isEmpty(),
                "O repositório deveria iniciar sem metas.");
    }

    @Quando("ele criar uma meta e preencher todos os campos corretamente")
    public void criar_meta_campos_corretos() {
        // Monta datas início/ fim válidas (fim > início)
        Calendar cal = Calendar.getInstance();
        Date inicio = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 7);
        Date fim = cal.getTime();

        // Cria uma Meta igual ao teu modelo
        metaCriada = new Meta(
                new MetaId(1),
                new ExercicioId(100),
                new TreinoId(200),
                "Meta de força 5x5",
                fim,
                inicio
        );

        metaRepository.save(metaCriada);
    }

    @Então("o sistema irá criar a meta com sucesso")
    public void sistema_cria_meta_com_sucesso() {
        assertNotNull(metaCriada);
        assertNotNull(metaCriada.getId());
        assertEquals("Meta de força 5x5", metaCriada.getNome());

        boolean presente = ((MetaMock) metaRepository)
                .findAll()
                .stream()
                .anyMatch(m -> m.getId().equals(metaCriada.getId()));

        assertTrue(presente, "A meta deveria estar persistida no mock.");
        System.out.println("Então: Meta criada com sucesso: " + metaCriada.getNome());
    }

    // ---------- Cenário 2 ----------

    @Dado("que eu não consiga concluir as metas no prazo")
    public void meta_nao_concluida_no_prazo() {
        metaRepository = new MetaMock();
        rewardService  = new RewardServiceMock();
        metaRepository.deleteAll();

        // Criamos uma meta com fim no passado para simular prazo estourado
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, -10);
        Date inicioAntigo = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 3); // fim 7 dias atrás
        Date fimPassado = cal.getTime();

        Meta metaExpirada = new Meta(
                new MetaId(2),
                new ExercicioId(101),
                new TreinoId(201),
                "Meta cardio 10km",
                fimPassado,
                inicioAntigo
        );

        metaRepository.save(metaExpirada);
        this.metaCriada = metaExpirada; // reaproveita campo
    }

    @Quando("eu tentar coletar as recompensas")
    public void tentar_coletar_recompensas() {
        LocalDate hoje = LocalDate.now();
        this.podeColetar = rewardService.canCollectRewards(metaCriada, hoje);
    }

    @Então("elas não estarão disponíveis para serem coletadas")
    public void recompensas_indisponiveis() {
        assertNotNull(podeColetar, "Deveria ter avaliado a coleta.");
        assertFalse(podeColetar, "Recompensas não deveriam estar disponíveis após o prazo.");
        System.out.println("Então: Recompensas bloqueadas pois a meta venceu sem conclusão.");
    }
}
