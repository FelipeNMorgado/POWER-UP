package Up.Power;

import Up.Power.avatar.AvatarId;
import Up.Power.avatar.AvatarRepository;
import Up.Power.avatar.ExperienceService;
import Up.Power.mocks.AvatarMock;
import Up.Power.mocks.ExperienceServiceMock;
import Up.Power.perfil.PerfilId;

import io.cucumber.java.en.*;
import static org.junit.jupiter.api.Assertions.*;

public class AvatarFeature {

    private AvatarMock avatarRepository;
    private ExperienceService experienceService;
    private Avatar avatarDoUsuario;

    // ---------- Cenário 1: Avanço de níveis ----------

    @Given("que o sistema possua um usuário ativo com um avatar no nível {int} e {int} de XP")
    public void usuario_ativo_com_avatar(int nivelInicial, int xpInicial) {
        avatarRepository = new AvatarMock();
        experienceService = new ExperienceServiceMock();
        avatarRepository.deleteAll(); // Garante que o teste comece limpo

        // Cria o avatar com os dados iniciais do cenário
        PerfilId perfilId = new PerfilId(10); // ID de perfil para o usuário
        avatarDoUsuario = new Avatar(new AvatarId(1), perfilId);
        avatarDoUsuario.setNivel(nivelInicial);
        avatarDoUsuario.setExperiencia(xpInicial);

        avatarRepository.save(avatarDoUsuario);
    }

    @When("o usuário receber {int} de XP")
    public void usuario_receber_xp(int xpGanha) {
        experienceService.adicionarXp(avatarDoUsuario, xpGanha);
    }

    @Then("o nível do avatar do usuário subirá para o nível {int}")
    public void nivel_do_avatar_subira(int nivelEsperado) {
        assertEquals(nivelEsperado, avatarDoUsuario.getNivel(), "O nível do avatar não subiu como esperado.");
        System.out.println("Então: Nível atualizado para " + avatarDoUsuario.getNivel() + " com sucesso.");
    }

    // ---------- Cenário 2: Comparação de atributos ----------

    @Given("que o avatar de um usuário possui {int} pontos de força")
    public void avatar_com_pontos_de_forca(int forcaInicial) {
        avatarRepository = new AvatarMock();
        experienceService = new ExperienceServiceMock();
        avatarRepository.deleteAll();

        PerfilId perfilId = new PerfilId(20);
        avatarDoUsuario = new Avatar(new AvatarId(2), perfilId);
        avatarDoUsuario.setForca(forcaInicial);

        avatarRepository.save(avatarDoUsuario);
    }

    @When("o usuário progredir de carga e receber um bônus de {int} pontos de força")
    public void usuario_receber_bonus_forca(int bonusDeForca) {
        experienceService.adicionarForca(avatarDoUsuario, bonusDeForca);
    }

    @Then("o sistema informa que o atributo de força do seu avatar está em {int}")
    public void sistema_informa_nova_forca(int forcaEsperada) {
        assertEquals(forcaEsperada, avatarDoUsuario.getForca(), "O atributo de força do avatar não foi atualizado corretamente.");
        System.out.println("Então: Atributo de força atualizado para " + avatarDoUsuario.getForca() + " com sucesso.");
    }
}