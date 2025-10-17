package Up.Power;

import Up.Power.*;
import Up.Power.exercicio.ExercicioId;
import Up.Power.planoTreino.PlanoTId;
import Up.Power.treino.TipoTreino;
import Up.Power.treino.TreinoId;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.*;

public class ControleIntensidadeTeste {

    private PlanoTreino plano;
    private Exercicio exercicio;
    private boolean novoRecorde;

    @Before
    public void setUp() {
        plano = new PlanoTreino(
                new PlanoTId(1),
                new Email("usuario@email.com"),
                "Plano Hipertrofia"
        );
        exercicio = new Exercicio(new ExercicioId(1), "Supino Reto");
    }

    // -----------------------------------------------------------
    // Cenário 1 — Recorde de levantamento
    // -----------------------------------------------------------

    @Given("que um usuario ja tenh realizado um exercicio")
    public void que_um_usuario_ja_tenh_realizado_um_exercicio() {
        // Dado que ele já tem um recorde anterior de 80kg
        Treino treinoAntigo = new Treino(
                new TreinoId(1),
                exercicio.getId(1),
                TipoTreino.Peso,
                10,
                80f,
                3,
                60
        );
        plano.atualizarRecorde(treinoAntigo);
    }

    @When("ele pega o maior carga que ele ja pegou")
    public void ele_pega_o_maior_carga_que_ele_ja_pegou() {
        // Quando o usuário levanta mais peso que o recorde
        Treino treinoNovo = new Treino(
                new TreinoId(2),
                exercicio.getId(1),
                TipoTreino.Peso,
                10,
                90f,
                3,
                60
        );
        novoRecorde = plano.atualizarRecorde(treinoNovo);
    }

    @Then("o sistema atualiza o seu peso record e o parabeniza")
    public void o_sistema_atualiza_o_seu_peso_record_e_o_parabeniza() {
        // Então o sistema reconhece o novo recorde
        assertTrue(novoRecorde, "Deve atualizar o recorde quando há progresso.");
        assertEquals(90f, plano.getRecordeCarga(), "O recorde deve ser atualizado para 90kg.");
        System.out.println("🏆 Parabéns! Novo recorde registrado: " + plano.getRecordeCarga() + "kg!");
    }

    // -----------------------------------------------------------
    // Cenário 2 — Controle de progresso
    // -----------------------------------------------------------

    @Given("que um usuario já possua um plano de treino")
    public void que_um_usuario_ja_possua_um_plano_de_treino() {
        assertNotNull(plano, "O plano de treino deve existir.");
    }

    @When("evoluir nas minhas cargas e repetições")
    public void evoluir_nas_minhas_cargas_e_repeticoes() {
        // Simula dois treinos do mesmo exercício com aumento de peso
        plano.adicionarTreino(new Treino(
                new TreinoId(1),
                exercicio.getId(1),
                TipoTreino.Peso,
                10,
                60f,
                3,
                60
        ));

        plano.adicionarTreino(new Treino(
                new TreinoId(2),
                exercicio.getId(1),
                TipoTreino.Peso,
                10,
                70f,
                3,
                60
        ));
    }

    @Then("ter uma visualização de desenvolvimento das cargas e registro delas")
    public void ter_uma_visualizacao_de_desenvolvimento_das_cargas_e_registro_delas() {
        assertTrue(plano.estaProgredindo(), "Deve detectar progresso de carga.");
        System.out.println("📈 Progresso detectado: carga aumentou!");
    }
}
