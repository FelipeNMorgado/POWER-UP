package Up.Power;

import Up.Power.usuario.AmizadeService;
import Up.Power.mocks.AmizadeMock;
import Up.Power.usuario.UsuarioRepository;
import io.cucumber.java.en.*;
import org.junit.Assert;
import java.time.LocalDate;

public class AmizadeFeature {

    private UsuarioRepository repository;
    private AmizadeService amizadeService;
    private Usuario usuario1;
    private Usuario usuario2;
    private String mensagem;

    @Given("que o sistema possua mais de dois usuários disponíveis")
    public void sistema_possui_usuarios() {
        repository = new AmizadeMock();
        amizadeService = new AmizadeService(repository);

        usuario1 = new Usuario(new Email("joao@email.com"), "João", "123", LocalDate.of(2000, 5, 10));
        usuario2 = new Usuario(new Email("maria@email.com"), "Maria", "abc", LocalDate.of(1999, 2, 15));

        amizadeService.salvarUsuario(usuario1);
        amizadeService.salvarUsuario(usuario2);

        Assert.assertTrue(((AmizadeMock) repository).tamanho() >= 2);
    }

    @When("um usuário enviar um convite de amizade")
    public void enviar_convite() {
        mensagem = amizadeService.enviarConvite(usuario1, usuario2);
    }

    @And("o usuário que recebeu o convite aceita")
    public void aceitar_convite() {
        mensagem = amizadeService.aceitarConvite(usuario1, usuario2);
    }

    @Then("o sistema informa que os usuários são amigos")
    public void verificar_amizade_criada() {
        Assert.assertTrue(amizadeService.saoAmigos(usuario1, usuario2));
        Assert.assertEquals("Usuários agora são amigos", mensagem);
    }

    @Given("que um usuário possua no mínimo uma amizade")
    public void usuario_com_amizade() {
        sistema_possui_usuarios();
        amizadeService.aceitarConvite(usuario1, usuario2);
    }

    @When("um usuário optar por remover amizade")
    public void remover_amizade() {
        mensagem = amizadeService.removerAmizade(usuario1, usuario2);
    }

    @Then("o sistema informa que os usuários não são mais amigos")
    public void verificar_amizade_removida() {
        Assert.assertFalse(amizadeService.saoAmigos(usuario1, usuario2));
        Assert.assertEquals("Usuários não são mais amigos", mensagem);
    }

    @And("o usuário que recebeu o convite recusa")
    public void recusar_convite() {
        mensagem = amizadeService.recusarConvite(usuario1, usuario2);
    }

    @Then("o sistema informa que os usuários não são amigos")
    public void verificar_recusa() {
        Assert.assertEquals("Usuário recusou o pedido de amizade", mensagem);
        Assert.assertTrue(amizadeService.getAmizades().isEmpty());
    }
}