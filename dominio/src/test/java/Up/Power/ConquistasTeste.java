package Up.Power;

import Up.Power.conquista.*;
import Up.Power.exercicio.ExercicioId;
import Up.Power.treino.TreinoId;
import Up.Power.mocks.ConquistaMock;
import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class ConquistasTeste {

    private ConquistaService service;
    private ConquistaRepository repository;
    private Conquista conquistaForca;
    private boolean resultado;
    private String mensagem;

    // ================================================================
    // CENÁRIO 1 — Obter conquista
    // ================================================================
    @Given("que o sistema possua conquistas ativas")
    public void que_o_sistema_possua_conquistas_ativas() {
        repository = new ConquistaMock();
        service = new ConquistaService(repository);

        conquistaForca = new Conquista(
                new ConquistaId(1),
                new ExercicioId(1),
                new TreinoId(1),
                "Levantar 100kg no supino",
                "Força Bruta"
        );

        service.adicionarConquistaAtiva(conquistaForca);
        assertTrue(service.temConquistasAtivas());
    }

    @When("um usuario alcançar os requisitos da conquista")
    public void um_usuario_alcancar_os_requisitos_da_conquista() {
        resultado = service.avaliarConquista(conquistaForca, 120f);
        mensagem = service.getUltimaMensagem();
    }

    @Then("o sistema informa que a conquista foi concluída e envia as recompensas para o usuário")
    public void o_sistema_informa_que_a_conquista_foi_concluida_e_envia_as_recompensas() {
        assertTrue(resultado);
        assertEquals("Recompensa enviada!", mensagem);
        assertTrue(service.getConquistasConcluidas().contains(conquistaForca));
    }

    // ================================================================
    // CENÁRIO 2 — Escolher badge
    // ================================================================
    @Given("que o usuário possua no mínimo uma conquista concluída")
    public void que_o_usuario_possua_uma_conquista_concluida() {
        repository = new ConquistaMock();
        service = new ConquistaService(repository);

        conquistaForca = new Conquista(
                new ConquistaId(2),
                new ExercicioId(1),
                new TreinoId(1),
                "Levantar 100kg no supino",
                "Força Bruta"
        );

        service.adicionarConquistaAtiva(conquistaForca);
        service.avaliarConquista(conquistaForca, 150f); // conclui a conquista

        assertTrue(service.getConquistasConcluidas().contains(conquistaForca));
    }

    @When("o usuário tentar escolher uma badge que representa essa conquista")
    public void o_usuario_tentar_escolher_uma_badge_que_representa_essa_conquista() {
        resultado = service.escolherBadge(conquistaForca, "Badge de Força");
        mensagem = service.getUltimaMensagem();
    }

    @Then("o sistema informa que essa badge foi escolhida e estabelece a vantagem específica")
    public void o_sistema_informa_que_essa_badge_foi_escolhida_e_estabelece_a_vantagem_especifica() {
        assertTrue(resultado);
        assertEquals("Badge de Força", service.getBadgeAtual());
        assertEquals("Vantagem: +5% força", mensagem);
    }

    // ================================================================
    // CENÁRIO 3 — Falha ao reivindicar
    // ================================================================
    @When("o usuário não conseguir alcançar os requisitos da conquista")
    public void o_usuario_nao_conseguir_alcancar_os_requisitos_da_conquista() {
        repository = new ConquistaMock();
        service = new ConquistaService(repository);

        conquistaForca = new Conquista(
                new ConquistaId(3),
                new ExercicioId(1),
                new TreinoId(1),
                "Levantar 100kg no supino",
                "Força Bruta"
        );

        service.adicionarConquistaAtiva(conquistaForca);

        resultado = service.avaliarConquista(conquistaForca, 60f); // abaixo do requisito
        mensagem = service.getUltimaMensagem();
    }

    @Then("o sistema informa que a conquista ainda não está disponível para conclusão")
    public void o_sistema_informa_que_a_conquista_ainda_nao_esta_disponivel_para_conclusao() {
        assertFalse(resultado);
        assertEquals("Conquista ainda não disponível.", mensagem);
        assertFalse(service.getConquistasConcluidas().contains(conquistaForca));
    }
}
