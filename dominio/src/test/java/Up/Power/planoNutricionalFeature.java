package Up.Power;

import Up.Power.mocks.PlanoNutricionalMock;
import Up.Power.planoNutricional.*;
import io.cucumber.java.en.*;
import org.junit.Assert;

public class planoNutricionalFeature {

    private PlanoNutricional planoNutricional;
    private PlanoNutricionalRepository repository;
    private PlanoNutricionalService service;
    private PlanoNId id;
    private Objetivo objetivo;
    private String mensagem;
    private Email usuarioEmail;

    @Given("que um usuario criou seu plano nutricional e esta tudo preenchido")
    public void criar_plano_completo() {
        id = new PlanoNId(1);
        objetivo = Objetivo.Bulking;
        usuarioEmail = new Email("teste@example.com");
        repository = new PlanoNutricionalMock();
        service = new PlanoNutricionalService(repository);
    }

    @When("o usuario tentar salva-lo")
    public void salvar_plano() {
        try {
            service.criarPlano(id, objetivo, usuarioEmail);
            mensagem = "Plano salvo com sucesso";
        } catch (Exception e) {
            mensagem = e.getMessage();
        }
    }

    @Then("o sistema ira salva-lo com sucesso, podendo ser vizualiza-lo quando desejar")
    public void verificar_plano_salvo() {
        PlanoNutricional salvo = service.obterPlano(id);
        Assert.assertNotNull(salvo);
        Assert.assertEquals(objetivo, salvo.getObjetivo());
        Assert.assertEquals("Plano salvo com sucesso", mensagem);
    }

    @Given("que um usuario tenha um plano nutricional salvo e deseja edita-lo")
    public void plano_salvo_para_edicao() {
        repository = new PlanoNutricionalMock();
        service = new PlanoNutricionalService(repository);
        usuarioEmail = new Email("teste@example.com");
        planoNutricional = new PlanoNutricional(new PlanoNId(2), Objetivo.Cutting, usuarioEmail);
        service.criarPlano(planoNutricional.getId(), planoNutricional.getObjetivo(), usuarioEmail);
    }

    @When("o usario fizer alteracoes no plano e confirmar as mudancas")
    public void editar_plano() {
        try {
            service.modificarPlano(planoNutricional.getId(), Objetivo.Bulking, 3000);
            mensagem = "Plano alterado com sucesso";
        } catch (Exception e) {
            mensagem = e.getMessage();
        }
    }

    @Then("o sistema ira salvar as alteracoes feitas e mostrar a versao atualizada para o usario")
    public void verificar_plano_editado() {
        PlanoNutricional salvo = service.obterPlano(planoNutricional.getId());
        Assert.assertEquals(Objetivo.Bulking, salvo.getObjetivo());
        Assert.assertEquals("Plano alterado com sucesso", mensagem);
    }

    @Given("o usuario tem um plano nutricional salvo")
    public void usuario_tem_plano() {
        repository = new PlanoNutricionalMock();
        service = new PlanoNutricionalService(repository);
        usuarioEmail = new Email("teste@example.com");
        planoNutricional = new PlanoNutricional(new PlanoNId(3), Objetivo.Cutting, usuarioEmail);
        service.criarPlano(planoNutricional.getId(), planoNutricional.getObjetivo(), usuarioEmail);
    }

    @When("o usauario tentar colocar campos obricatorios em branco")
    public void tentar_colocar_campos_em_branco() {
        try {
            service.modificarPlano(planoNutricional.getId(),null, 500);
        } catch (Exception e) {
            mensagem = e.getMessage();
        }
    }

    @Then("o sistema nao salvara a tentativa de alteracao")
    public void verificar_erro_campos_em_branco() {
        Assert.assertEquals("Campos obrigatórios em branco", mensagem);
    }

    @Given("que o usuário possua um plano nutricional ativo")
    public void plano_ativo() {
        repository = new PlanoNutricionalMock();
        service = new PlanoNutricionalService(repository);
        usuarioEmail = new Email("teste@example.com");
        planoNutricional = new PlanoNutricional(new PlanoNId(4), Objetivo.Bulking, usuarioEmail);
        service.criarPlano(planoNutricional.getId(), planoNutricional.getObjetivo(), usuarioEmail);
    }

    @When("o usuario tentar modificar um campo especifico com valores nao esperados")
    public void modificar_com_valores_invalidos() {
        try {
            service.modificarPlano(planoNutricional.getId(), Objetivo.Bulking, -500);
        } catch (Exception e) {
            mensagem = e.getMessage();
        }
    }

    @Then("o sistema informa que nao foi possivel alterar o atributo")
    public void verificar_erro_modificacao_invalida() {
        Assert.assertEquals("Valor inválido para atributo", mensagem);
    }
}