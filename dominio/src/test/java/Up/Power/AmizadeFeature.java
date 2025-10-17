package Up.Power;

import Up.Power.mocks.AmizadeMock;
import Up.Power.usuario.UsuarioRepository;
import io.cucumber.java.en.*;
import org.junit.Assert;

import java.time.LocalDate;
import java.util.*;

public class AmizadeFeature {

    private UsuarioRepository repository;
    private Usuario usuario1;
    private Usuario usuario2;
    private Map<AmizadeId, Set<Usuario>> amizades; // Agora usa AmizadeId como chave real
    private String mensagem;

    @Given("que o sistema possua mais de dois usuários disponíveis")
    public void sistema_possui_usuarios() {
        repository = new AmizadeMock();
        amizades = new HashMap<>();

        usuario1 = new Usuario(new Email("joao@email.com"), "João", "123", LocalDate.of(2000, 5, 10));
        usuario2 = new Usuario(new Email("maria@email.com"), "Maria", "abc", LocalDate.of(1999, 2, 15));

        repository.salvar(usuario1);
        repository.salvar(usuario2);

        Assert.assertTrue(((AmizadeMock) repository).tamanho() >= 2);
    }

    @When("um usuário enviar um convite de amizade")
    public void enviar_convite() {
        mensagem = "Convite de amizade enviado";
    }

    @And("o usuário que recebeu o convite aceita")
    public void aceitar_convite() {
        // Cria um novo identificador de amizade
        AmizadeId amizadeId = new AmizadeId(1);
        usuario1.setCodigoAmizade(amizadeId);
        usuario2.setCodigoAmizade(amizadeId);

        // Cria o conjunto com os dois usuários e adiciona no mapa
        Set<Usuario> grupoAmizade = new HashSet<>(Set.of(usuario1, usuario2));
        amizades.put(amizadeId, grupoAmizade);

        mensagem = "Usuários agora são amigos";
    }

    @Then("o sistema informa que os usuários são amigos")
    public void verificar_amizade_criada() {
        AmizadeId amizadeId = usuario1.getCodigoAmizade();

        Assert.assertNotNull(amizadeId);
        Assert.assertTrue(amizades.containsKey(amizadeId));
        Assert.assertTrue(amizades.get(amizadeId).contains(usuario1));
        Assert.assertTrue(amizades.get(amizadeId).contains(usuario2));
        Assert.assertEquals("Usuários agora são amigos", mensagem);
    }

    @Given("que um usuário possua no mínimo uma amizade")
    public void usuario_com_amizade() {
        sistema_possui_usuarios();
        AmizadeId amizadeId = new AmizadeId(1);
        usuario1.setCodigoAmizade(amizadeId);
        usuario2.setCodigoAmizade(amizadeId);

        Set<Usuario> grupoAmizade = new HashSet<>(Set.of(usuario1, usuario2));
        amizades.put(amizadeId, grupoAmizade);
    }

    @When("um usuário optar por remover amizade")
    public void remover_amizade() {
        AmizadeId amizadeId = usuario1.getCodigoAmizade();
        if (amizadeId != null) {
            amizades.remove(amizadeId);
        }
        usuario1.setCodigoAmizade(null);
        usuario2.setCodigoAmizade(null);
        mensagem = "Usuários não são mais amigos";
    }

    @Then("o sistema informa que os usuários não são mais amigos")
    public void verificar_amizade_removida() {
        AmizadeId amizadeId = new AmizadeId(1);

        Assert.assertFalse(amizades.containsKey(amizadeId));
        Assert.assertNull(usuario1.getCodigoAmizade());
        Assert.assertNull(usuario2.getCodigoAmizade());
        Assert.assertEquals("Usuários não são mais amigos", mensagem);
    }

    @And("o usuário que recebeu o convite recusa")
    public void recusar_convite() {
        mensagem = "Usuário recusou o pedido de amizade";
    }

    @Then("o sistema informa que os usuários não são amigos")
    public void verificar_recusa() {
        Assert.assertEquals("Usuário recusou o pedido de amizade", mensagem);
        Assert.assertTrue(amizades.isEmpty());
    }
}
