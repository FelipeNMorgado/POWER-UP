package Up.Power;

import Up.Power.feedback.Classificacao;
import Up.Power.feedback.FeedbackId;
import Up.Power.feedback.FeedbackService;
import Up.Power.frequencia.FrequenciaId;
import Up.Power.mocks.FeedbackMock;
import io.cucumber.java.en.*;

import static org.junit.jupiter.api.Assertions.*;

public class feedbackTreinoFeature {

    private FeedbackMock mock;
    private FeedbackService service;
    private Feedback feedback;
    private FeedbackId feedbackId;
    private FrequenciaId frequenciaId;
    private final Email usuarioEmail = new Email("usuario@exemplo.com");

    // ================================================================
    // CENÁRIO 1 — Adicionar Feedback
    // ================================================================

    @Given("que o usuário tenha feito um treino")
    public void que_o_usuario_tenha_feito_um_treino() {
        mock = new FeedbackMock(null);
        service = new FeedbackService(mock);
        frequenciaId = new FrequenciaId(1);
    }

    @When("um usuário tentar adicionar uma descrição {string} para o treino")
    public void um_usuario_tentar_adicionar_uma_descricao_para_o_treino(String descricao) {
        feedbackId = new FeedbackId(1);
        // Adiciona inicialmente sem classificação (ou com padrão)
        feedback = service.adicionarFeedback(
                feedbackId,
                frequenciaId,
                usuarioEmail,
                descricao,
                Classificacao.Bom // valor padrão inicial
        );
    }

    @And("escolher a categoria como {string}")
    public void escolher_a_categoria_como(String classificacaoStr) {
        Classificacao classificacao = Classificacao.valueOf(classificacaoStr);
        feedback = service.editarFeedback(feedbackId, feedback.getFeedback(), classificacao);
    }

    @Then("o sistema informa que o treino possui a descrição {string}")
    public void o_sistema_informa_que_o_treino_possui_a_descricao(String descricaoEsperada) {
        Feedback obtido = service.obterFeedback(feedbackId);
        assertNotNull(obtido, "Feedback não encontrado");
        assertEquals(descricaoEsperada, obtido.getFeedback());
    }

    @And("a categoria {string}")
    public void a_categoria(String classificacaoEsperada) {
        Feedback obtido = service.obterFeedback(feedbackId);
        assertNotNull(obtido, "Feedback não encontrado");
        Classificacao classificacao = Classificacao.valueOf(classificacaoEsperada);
        assertEquals(classificacao, obtido.getClassificacao());
    }

    // ================================================================
    // CENÁRIO 2 — Editar Feedback
    // ================================================================

    @Given("que um usuário possua um treino com uma descrição {string} em um treino com categoria {string}")
    public void que_um_usuario_possua_um_treino_com_uma_descricao_em_um_treino_com_categoria(
            String descricao, String classificacaoStr) {

        mock = new FeedbackMock(null);
        service = new FeedbackService(mock);
        frequenciaId = new FrequenciaId(2);
        feedbackId = new FeedbackId(2);

        Classificacao classificacao = Classificacao.valueOf(classificacaoStr);

        feedback = service.adicionarFeedback(feedbackId, frequenciaId, usuarioEmail, descricao, classificacao);
    }

    @When("um usuário optar por editar essa descrição para {string}")
    public void um_usuario_optar_por_editar_essa_descricao_para(String novaDescricao) {
        feedback = service.editarFeedback(feedbackId, novaDescricao, feedback.getClassificacao());
    }

    @And("editar categoria para {string}")
    public void editar_categoria_para(String novaClassificacaoStr) {
        Classificacao novaClassificacao = Classificacao.valueOf(novaClassificacaoStr);
        feedback = service.editarFeedback(feedbackId, feedback.getFeedback(), novaClassificacao);
    }

    @Then("o sistema informa que o treino possui essa nova descrição e categoria")
    public void o_sistema_informa_que_o_treino_possui_essa_nova_descricao_e_categoria() {
        Feedback obtido = service.obterFeedback(feedbackId);
        assertNotNull(obtido, "Feedback não encontrado");
        assertEquals(feedback.getFeedback(), obtido.getFeedback());
        assertEquals(feedback.getClassificacao(), obtido.getClassificacao());
    }

    // ================================================================
    // CENÁRIO 3 — Excluir Feedback
    // ================================================================

    @Given("que um usuário possua uma descrição em um treino")
    public void que_um_usuario_possua_uma_descricao_em_um_treino() {
        mock = new FeedbackMock(null);
        service = new FeedbackService(mock);
        frequenciaId = new FrequenciaId(3);
        feedbackId = new FeedbackId(3);

        feedback = service.adicionarFeedback(
                feedbackId,
                frequenciaId,
                usuarioEmail,
                "Descrição qualquer",
                Classificacao.Bom
        );
    }

    @When("um usuário optar por excluir essa descrição")
    public void um_usuario_optar_por_excluir_essa_descricao() {
        service.excluirFeedback(feedbackId);
    }

    @Then("o sistema informa que o treino deixou de ter a descrição anterior")
    public void o_sistema_informa_que_o_treino_deixou_de_ter_a_descricao_anterior() {
        Feedback obtido = service.obterFeedback(feedbackId);
        assertNull(obtido, "O feedback ainda existe após exclusão");
    }
}