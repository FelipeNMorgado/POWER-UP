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

    // Repositórios Mocks e Serviços
    private PerfilRepository perfilRepositoryMock;
    private AvatarRepository avatarRepositoryMock;
    private DueloRepository dueloRepositoryMock;
    private DueloService dueloService;

    // Variáveis para guardar o estado do cenário
    private Map<String, Perfil> perfis = new HashMap<>();
    private Duelo dueloResultado;
    private Exception excecaoOcorrida;

    // ... (Todos os steps dos cenários anteriores de Rivalidade e Duelo continuam aqui)


    // --- NOVOS PASSOS PARA O CENÁRIO "CONSEGUIR DUELAR" ---

    @Dado("que um usuario não tenha feito um deulo contra o amigo desafidado na semana")
    public void que_um_usuario_nao_tenha_feito_um_duelo_na_semana() {
        // 1. Setup dos mocks e serviços
        perfilRepositoryMock = new PerfilMock();
        avatarRepositoryMock = new AvatarMock();
        dueloRepositoryMock = new DueloMock();

        AvatarService avatarService = new AvatarService(avatarRepositoryMock);
        dueloService = new DueloService(dueloRepositoryMock, avatarService, perfilRepositoryMock, avatarRepositoryMock);

        // 2. Criação dos perfis
        Perfil desafiante = new Perfil(new PerfilId(1), new Email("desafiante@teste.com"), "Desafiante");
        Perfil desafiado = new Perfil(new PerfilId(2), new Email("desafiado@teste.com"), "Desafiado");

        perfis.put("Desafiante", desafiante);
        perfis.put("Desafiado", desafiado);

        // 3. Estabelece a amizade entre eles
        desafiante.adicionarAmigo(new Usuario(desafiado.getUsuarioEmail(), desafiado.getUsername(), "senha123", LocalDate.now()));
        perfilRepositoryMock.save(desafiante);
        perfilRepositoryMock.save(desafiado);

        // 4. Cria avatares com níveis diferentes para ter um vencedor claro
        // Desafiante (nível 10) deve vencer o Desafiado (nível 5)
        Avatar avatarDesafiante = new Avatar(new AvatarId(1), desafiante.getId());
        avatarDesafiante.setNivel(10); // Adicione um setNivel na classe Avatar para testes

        Avatar avatarDesafiado = new Avatar(new AvatarId(2), desafiado.getId());
        avatarDesafiado.setNivel(5);

        avatarRepositoryMock.save(avatarDesafiante);
        avatarRepositoryMock.save(avatarDesafiado);

        System.out.println("Dado: Usuários 'Desafiante' e 'Desafiado' são amigos e estão prontos para duelar.");
        // Nota: Não criamos nenhum duelo prévio, o que satisfaz a condição "não ter duelado na semana".
    }

    @Quando("um usuario tentar iniciar um duelo com outro usuário")
    public void um_usuario_tentar_iniciar_um_duelo_com_outro_usuario() {
        try {
            Perfil desafiante = perfis.get("Desafiante");
            Perfil desafiado = perfis.get("Desafiado");

            // Executa a ação principal do cenário
            this.dueloResultado = dueloService.iniciarDuelo(desafiante.getId(), desafiado.getId());

            System.out.println("Quando: 'Desafiante' iniciou um duelo com 'Desafiado'.");

        } catch (Exception e) {
            this.excecaoOcorrida = e;
        }
    }

    @Entao("o sistema iniciara o duelo e fara o calculo do vencedor")
    public void o_sistema_iniciara_o_duelo_e_fara_o_calculo_do_vencedor() {
        // 1. Verifica se nenhuma exceção foi lançada, pois este é o caminho feliz.
        assertNull(excecaoOcorrida, "Uma exceção não deveria ter ocorrido: " + (excecaoOcorrida != null ? excecaoOcorrida.getMessage() : ""));

        // 2. Verifica se o duelo foi registrado e tem um ID.
        assertNotNull(dueloResultado, "O duelo deveria ter sido criado.");
        assertNotNull(dueloResultado.getId(), "O duelo salvo deveria ter um ID.");

        // 3. Verifica se o resultado foi calculado e é o esperado.
        // Com a nossa lógica (nível 10 vs 5), o desafiante sempre ganha.
        assertNotNull(dueloResultado.getResultado(), "O resultado do duelo não foi calculado.");
        assertEquals("VITORIA_DESAFIANTE", dueloResultado.getResultado(), "O desafiante deveria ter vencido o duelo.");

        System.out.println("Então: Duelo iniciado com sucesso! O vencedor foi: " + dueloResultado.getResultado());
    }

    @Dado("um usuário faca um desafio e o prazo entre um duelo e outro nao tiver acabado")
    public void um_usuario_faca_um_desafio_e_o_prazo_nao_tiver_acabado() {
        // 1. Setup dos mocks e serviços (como nos outros cenários)
        perfilRepositoryMock = new PerfilMock();
        avatarRepositoryMock = new AvatarMock();
        dueloRepositoryMock = new DueloMock();

        AvatarService avatarService = new AvatarService(avatarRepositoryMock);
        dueloService = new DueloService(dueloRepositoryMock, avatarService, perfilRepositoryMock, avatarRepositoryMock);

        // 2. Criação dos perfis e avatares
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

        // 3. A PARTE CRÍTICA: Criar e salvar um duelo que aconteceu HÁ 3 DIAS
        Duelo dueloAntigo = new Duelo(avatarDesafiante.getId(), avatarDesafiado.getId());
        dueloAntigo.setDataDuelo(LocalDateTime.now().minusDays(3)); // <-- Usando o setter para forjar a data
        dueloAntigo.setResultado("EMPATE"); // Resultado qualquer
        dueloRepositoryMock.save(dueloAntigo);

        System.out.println("Dado: 'Desafiante' e 'Desafiado' duelaram há 3 dias. O cooldown está ativo.");
    }

    @Quando("um usario tentar desafiar outro")
    public void um_usario_tentar_desafiar_outro() {
        try {
            Perfil desafiante = perfis.get("Desafiante");
            Perfil desafiado = perfis.get("Desafiado");

            // Esta chamada DEVE lançar uma exceção por causa da regra de negócio
            dueloService.iniciarDuelo(desafiante.getId(), desafiado.getId());

        } catch (Exception e) {
            // Capturamos a exceção esperada para verificar no passo 'Then'
            this.excecaoOcorrida = e;
        }
        System.out.println("Quando: 'Desafiante' tentou desafiar 'Desafiado' novamente.");
    }

    @Então("o sistema informa que é impossível duelar no momento")
    public void o_sistema_informa_que_e_impossivel_duelar_no_momento() {
        // 1. Verifica se a exceção esperada realmente aconteceu.
        assertNotNull(excecaoOcorrida, "Uma exceção deveria ter sido lançada devido ao cooldown, mas não foi.");

        // 2. Verifica se a exceção é do tipo correto.
        assertInstanceOf(IllegalStateException.class, excecaoOcorrida, "A exceção deveria ser do tipo 'IllegalStateException'.");

        // 3. Verifica se a mensagem de erro é a correta, confirmando que a regra de cooldown foi acionada.
        // A nossa mensagem no serviço calcula os dias restantes.
        assertTrue(
                excecaoOcorrida.getMessage().contains("dias para desafiar este amigo novamente"),
                "A mensagem de erro não corresponde à regra de cooldown."
        );

        // 4. (Opcional, mas recomendado) Garante que nenhum novo duelo foi criado
        long totalDeDuelos = ((DueloMock) dueloRepositoryMock).findAll().size();
        assertEquals(1, totalDeDuelos, "Nenhum novo duelo deveria ter sido salvo no banco.");

        System.out.println("Então: Sistema bloqueou o duelo corretamente com a mensagem: \"" + excecaoOcorrida.getMessage() + "\"");
    }

}