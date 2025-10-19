package Up.Power;

import Up.Power.avatar.AvatarId;
import Up.Power.avatar.AvatarRepository;
import Up.Power.avatar.AvatarService;
import Up.Power.duelo.DueloRepository;
import Up.Power.duelo.DueloService;
import Up.Power.mocks.AvatarMock;
import Up.Power.mocks.DueloMock;
import Up.Power.mocks.PerfilMock;
import Up.Power.perfil.PerfilId;
import Up.Power.perfil.PerfilRepository;
import io.cucumber.java.pt.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class DueloFeature {

    private PerfilRepository perfilRepositoryMock;
    private AvatarRepository avatarRepositoryMock;
    private DueloRepository dueloRepositoryMock;
    private DueloService dueloService;

    private Map<String, Perfil> perfis = new HashMap<>();
    private Duelo dueloResultado;
    private Exception excecaoOcorrida;

    @Dado("que um usuario não tenha feito um duelo contra o amigo desafidado na semana")
    public void que_um_usuario_nao_tenha_feito_um_duelo_na_semana() {
        perfilRepositoryMock = new PerfilMock();
        avatarRepositoryMock = new AvatarMock();
        dueloRepositoryMock = new DueloMock();

        AvatarService avatarService = new AvatarService(avatarRepositoryMock);
        dueloService = new DueloService(dueloRepositoryMock, avatarService ,perfilRepositoryMock, avatarRepositoryMock);

        Perfil desafiante = new Perfil(new PerfilId(1), new Email("desafiante@teste.com"), "Desafiante");
        Perfil desafiado = new Perfil(new PerfilId(2), new Email("desafiado@teste.com"), "Desafiado");

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

    @Quando("um usuario tentar iniciar um duelo com outro usuário")
    public void um_usuario_tentar_iniciar_um_duelo_com_outro_usuario() {
        try {
            Perfil desafiante = perfis.get("Desafiante");
            Perfil desafiado = perfis.get("Desafiado");

            this.dueloResultado = dueloService.realizarDuelo(desafiante.getId(), desafiado.getId());

            System.out.println("Quando: 'Desafiante' iniciou um duelo com 'Desafiado'.");

        } catch (Exception e) {
            this.excecaoOcorrida = e;
        }
    }

    @Entao("o sistema iniciara o duelo e fara o calculo do vencedor")
    public void o_sistema_iniciara_o_duelo_e_fara_o_calculo_do_vencedor() {
        assertNull(excecaoOcorrida, "Uma exceção não deveria ter ocorrido: " + (excecaoOcorrida != null ? excecaoOcorrida.getMessage() : ""));

        assertNotNull(dueloResultado, "O duelo deveria ter sido criado.");
        assertNotNull(dueloResultado.getId(), "O duelo salvo deveria ter um ID.");

        assertNotNull(dueloResultado.getResultado(), "O resultado do duelo não foi calculado.");
        assertEquals("VITORIA_DESAFIANTE", dueloResultado.getResultado(), "O desafiante deveria ter vencido o duelo.");

        System.out.println("Então: Duelo iniciado com sucesso! O vencedor foi: " + dueloResultado.getResultado());
    }

    @Dado("um usuário faca um desafio e o prazo entre um duelo e outro nao tiver acabado")
    public void um_usuario_faca_um_desafio_e_o_prazo_nao_tiver_acabado() {
        perfilRepositoryMock = new PerfilMock();
        avatarRepositoryMock = new AvatarMock();
        dueloRepositoryMock = new DueloMock();

        AvatarService avatarService = new AvatarService(avatarRepositoryMock);
        dueloService = new DueloService(dueloRepositoryMock, avatarService ,perfilRepositoryMock, avatarRepositoryMock);

        Perfil desafiante = new Perfil(new PerfilId(8), new Email("desafiante@teste.com"), "Desafiante");
        Perfil desafiado = new Perfil(new PerfilId(9), new Email("desafiado@teste.com"), "Desafiado");

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

    @Quando("um usario tentar desafiar outro")
    public void um_usario_tentar_desafiar_outro() {
        try {
            Perfil desafiante = perfis.get("Desafiante");
            Perfil desafiado = perfis.get("Desafiado");

            dueloService.realizarDuelo(desafiante.getId(), desafiado.getId());

        } catch (Exception e) {
            this.excecaoOcorrida = e;
        }
        System.out.println("Quando: 'Desafiante' tentou desafiar 'Desafiado' novamente.");
    }

    @Entao("o sistema informa que é impossível duelar no momento")
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