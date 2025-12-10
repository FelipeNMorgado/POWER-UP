package Up.Power;

import Up.Power.avatar.AvatarId;
import Up.Power.avatar.AvatarRepository;
import Up.Power.avatar.AvatarService;
import Up.Power.duelo.DueloId;
import Up.Power.duelo.DueloRepository;
import Up.Power.duelo.DueloService;
import Up.Power.mocks.AvatarMock;
import Up.Power.mocks.DueloMock;
import Up.Power.mocks.PerfilMock;
import Up.Power.perfil.PerfilId;
import Up.Power.perfil.PerfilRepository;
import io.cucumber.java.en.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class DueloFeature {

    private PerfilRepository perfilRepositoryMock;
    private AvatarRepository avatarRepositoryMock;
    private DueloRepository dueloRepositoryMock;
    private DueloService dueloService;

    private Map<String, Perfil> perfis = new HashMap<>();
    private Duelo dueloResultado;
    private Exception excecaoOcorrida;

    @Given("que um usuario {string} não tenha feito um duelo contra o amigo {string} na semana")
    public void que_um_usuario_nao_tenha_feito_um_duelo_na_semana(String nomeDesafiante, String nomeDesafiado) {
        perfilRepositoryMock = new PerfilMock();
        avatarRepositoryMock = new AvatarMock();
        dueloRepositoryMock = new DueloMock();

        AvatarService avatarService = new AvatarService(avatarRepositoryMock);
        dueloService = new DueloService(dueloRepositoryMock, avatarService ,perfilRepositoryMock, avatarRepositoryMock);

        Perfil desafiante = new Perfil(new PerfilId(1), new Email("desafiante@teste.com"), nomeDesafiante);
        Perfil desafiado = new Perfil(new PerfilId(2), new Email("desafiado@teste.com"), nomeDesafiado);

        perfis.put("Desafiante", desafiante);
        perfis.put("Desafiado", desafiado);

        desafiante.adicionarAmigo(new Usuario(desafiado.getUsuarioEmail(), desafiado.getUsername(), "senha123", LocalDate.now()));
        perfilRepositoryMock.save(desafiante);
        perfilRepositoryMock.save(desafiado);

        Avatar avatarDesafiante = new Avatar(new AvatarId(1), desafiante.getId());
        avatarDesafiante.setNivel(10);

        Avatar avatarDesafiado = new Avatar(new AvatarId(2), desafiado.getId());
        avatarDesafiado.setNivel(5);

        avatarRepositoryMock.save(avatarDesafiante);
        avatarRepositoryMock.save(avatarDesafiado);

        System.out.println("Dado: Usuários 'Desafiante' e 'Desafiado' são amigos e estão prontos para duelar.");
    }

    @When("o usuario {string} tentar iniciar um duelo contra o usuario {string}")
    public void um_usuario_tentar_iniciar_um_duelo_com_outro_usuario(String nomeDesafiante, String nomeDesafiado) {
        try {
            Perfil desafiante = perfis.get("Desafiante");
            Perfil desafiado = perfis.get("Desafiado");

            this.dueloResultado = dueloService.realizarDuelo(desafiante.getId(), desafiado.getId());

            System.out.println("Quando: 'Desafiante' iniciou um duelo com 'Desafiado'.");

        } catch (Exception e) {
            this.excecaoOcorrida = e;
        }
    }

    @Then("o sistema iniciara o duelo e fara o calculo do vencedor")
    public void o_sistema_iniciara_o_duelo_e_fara_o_calculo_do_vencedor() {
        assertNull(excecaoOcorrida, "Uma exceção não deveria ter ocorrido.");

        assertNotNull(this.dueloResultado, "O serviço deveria ter retornado um resultado de duelo.");
        DueloId idDoDueloSalvo = this.dueloResultado.getId();
        assertNotNull(idDoDueloSalvo, "O duelo retornado deveria ter um ID gerado pelo save.");

        Optional<Duelo> dueloEncontradoOptional = dueloRepositoryMock.findById(idDoDueloSalvo);
        assertTrue(dueloEncontradoOptional.isPresent(), "O duelo não foi encontrado no repositório após ser salvo.");

        Duelo dueloEncontradoNoRepo = dueloEncontradoOptional.get();
        assertEquals(this.dueloResultado, dueloEncontradoNoRepo);
        assertEquals("VITORIA_DESAFIANTE(A1)", dueloEncontradoNoRepo.getResultado());

        System.out.println("Então: Duelo iniciado com sucesso e confirmado no repositório!");
    }

    // --------------------Cenario 2: Tentar duelar antes de acabar o cooldown--------------------------- //

    @Given("um usuário {string} teve um duelo com o amigo {string} a menos de uma semana")
    public void um_usuario_faca_um_desafio_e_o_prazo_nao_tiver_acabado(String nomeDesafiante, String nomeDesafiado) {
        perfilRepositoryMock = new PerfilMock();
        avatarRepositoryMock = new AvatarMock();
        dueloRepositoryMock = new DueloMock();

        AvatarService avatarService = new AvatarService(avatarRepositoryMock);
        dueloService = new DueloService(dueloRepositoryMock, avatarService ,perfilRepositoryMock, avatarRepositoryMock);

        Perfil desafiante = new Perfil(new PerfilId(8), new Email("desafiante@teste.com"), nomeDesafiante);
        Perfil desafiado = new Perfil(new PerfilId(9), new Email("desafiado@teste.com"), nomeDesafiado);

        perfis.put("Desafiante", desafiante);
        perfis.put("Desafiado", desafiado);

        desafiante.adicionarAmigo(new Usuario(desafiado.getUsuarioEmail(), desafiado.getUsername(), "s", LocalDate.now()));
        perfilRepositoryMock.save(desafiante);
        perfilRepositoryMock.save(desafiado);

        Avatar avatarDesafiante = new Avatar(new AvatarId(22), desafiante.getId());
        avatarDesafiante.setNivel(10);
        Avatar avatarDesafiado = new Avatar(new AvatarId(55), desafiado.getId());
        avatarDesafiado.setNivel(10);
        avatarRepositoryMock.save(avatarDesafiante);
        avatarRepositoryMock.save(avatarDesafiado);

        Duelo dueloAntigo = new Duelo(avatarDesafiante.getId(), avatarDesafiado.getId());
        dueloAntigo.setDataDuelo(LocalDateTime.now().minusDays(3));
        dueloAntigo.setResultado("EMPATE");
        dueloRepositoryMock.save(dueloAntigo);

        System.out.println("Dado: 'Desafiante' e 'Desafiado' duelaram há 3 dias. O cooldown está ativo.");
    }

    @When("o usario {string} tentar desafiar o amigo {string} novamente")
    public void um_usario_tentar_desafiar_outro(String nomeDesafiante, String nomeDesafiado) {
        try {
            Perfil desafiante = perfis.get("Desafiante");
            Perfil desafiado = perfis.get("Desafiado");

            dueloService.realizarDuelo(desafiante.getId(), desafiado.getId());

        } catch (Exception e) {
            this.excecaoOcorrida = e;
        }
        System.out.println("Quando: 'Desafiante' tentou desafiar 'Desafiado' novamente.");
    }

    @Then("o sistema informa que é impossível duelar no momento")
    public void o_sistema_informa_que_e_impossivel_duelar_no_momento() {
        assertNotNull(excecaoOcorrida, "Uma exceção deveria ter sido lançada devido ao cooldown, mas não foi.");

        assertInstanceOf(IllegalStateException.class, excecaoOcorrida, "A exceção deveria ser do tipo 'IllegalStateException'.");

        assertTrue(
                excecaoOcorrida.getMessage().contains("dias para desafiar este amigo novamente"),
                "A mensagem de erro não corresponde à regra de cooldown."
        );

        long totalDeDuelos = ((DueloMock) dueloRepositoryMock).findAll().size();
        assertEquals(1, totalDeDuelos, "Nenhum novo duelo deveria ter sido salvo no banco.");

        System.out.println("Então: Sistema bloqueou o duelo corretamente com a mensagem: \"" + excecaoOcorrida.getMessage() + "\"");
    }

}