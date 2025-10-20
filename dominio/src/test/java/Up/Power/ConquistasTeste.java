package Up.Power;

import Up.Power.conquista.ConquistaId;
import Up.Power.conquista.ConquistaService;
import Up.Power.conquista.ConquistaRepository;
import Up.Power.exercicio.ExercicioId;
import Up.Power.treino.TreinoId;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class ConquistasTeste {

    private ConquistaService gerenciador;
    private ConquistaRepository repository;
    private Conquista conquistaForca;
    private boolean resultado;
    private String mensagem;

    // Cenário 1: obtendo conquista
    @Given("que o sistema possua conquistas ativas")
    public void que_o_sistema_possua_conquistas_ativas() {
        repository = mock(ConquistaRepository.class);
        gerenciador = new ConquistaService(repository);

        conquistaForca = new Conquista(
                new ConquistaId(1),
                new ExercicioId(1),
                new TreinoId(1),
                "Levantar 100kg no supino",
                "Força Bruta"
        );

        // simula que o repository retorna conquistas ativas
        when(repository.listarAtivas()).thenReturn(List.of(conquistaForca));

        gerenciador.adicionarConquistaAtiva(conquistaForca);

        // verifica se o Service chamou salvar no Repository
        verify(repository).salvar(conquistaForca);

        assertTrue(gerenciador.temConquistasAtivas());
    }

    @When("um usuario alcançar os requisitos da conquista")
    public void um_usuario_alcancar_os_requisitos_da_conquista() {
        resultado = gerenciador.avaliarConquista(conquistaForca, 120f);
        mensagem = gerenciador.getUltimaMensagem();
    }

    @Then("o sistema informa que a conquista foi concluída e envia as recompensas para o usuário")
    public void o_sistema_informa_que_a_conquista_foi_concluida_e_envia_as_recompensas() {
        assertTrue(resultado);
        assertEquals("Recompensa enviada!", mensagem);

        // verifica se o Service chamou marcarComoConcluida
        verify(repository).marcarComoConcluida(conquistaForca);
    }

    // Cenário 2: escolher badge
    @Given("que o usuário possua no mínimo uma conquista concluída")
    public void que_o_usuario_possua_uma_conquista_concluida() {
        repository = mock(ConquistaRepository.class);
        gerenciador = new ConquistaService(repository);

        conquistaForca = new Conquista(
                new ConquistaId(1),
                new ExercicioId(1),
                new TreinoId(1),
                "Levantar 100kg no supino",
                "Força Bruta"
        );

        // simula que o repository retorna conquistas concluídas
        when(repository.listarConcluidas()).thenReturn(List.of(conquistaForca));

        assertTrue(gerenciador.getConquistasConcluidas().contains(conquistaForca));
    }

    @When("o usuário tentar escolher uma badge que representa essa conquista")
    public void o_usuario_tentar_escolher_uma_badge_que_representa_essa_conquista() {
        resultado = gerenciador.escolherBadge(conquistaForca, "Badge de Força");
        mensagem = gerenciador.getUltimaMensagem();
    }

    @Then("o sistema informa que essa badge foi escolhida e estabelece a vantagem específica")
    public void o_sistema_informa_que_essa_badge_foi_escolhida_e_estabelece_a_vantagem_especifica() {
        assertTrue(resultado);
        assertEquals("Badge de Força", gerenciador.getBadgeAtual());
        assertEquals("Vantagem: +5% força", mensagem);
    }

    // Cenário 3: falha ao reivindicar
    @When("o usuário não conseguir alcançar os requisitos da conquista")
    public void o_usuario_nao_conseguir_alcancar_os_requisitos_da_conquista() {
        repository = mock(ConquistaRepository.class);
        gerenciador = new ConquistaService(repository);

        conquistaForca = new Conquista(
                new ConquistaId(1),
                new ExercicioId(1),
                new TreinoId(1),
                "Levantar 100kg no supino",
                "Força Bruta"
        );

        // simula que não há conquistas concluídas ainda
        when(repository.listarAtivas()).thenReturn(List.of(conquistaForca));
        when(repository.listarConcluidas()).thenReturn(List.of());

        resultado = gerenciador.avaliarConquista(conquistaForca, 60f);
        mensagem = gerenciador.getUltimaMensagem();
    }

    @Then("o sistema informa que a conquista ainda não está disponível para conclusão")
    public void o_sistema_informa_que_a_conquista_ainda_nao_esta_disponivel_para_conclusao() {
        assertFalse(resultado);
        assertEquals("Conquista ainda não disponível.", mensagem);

        // verifica que marcarComoConcluida não foi chamado
        verify(repository, never()).marcarComoConcluida(conquistaForca);
    }
}
