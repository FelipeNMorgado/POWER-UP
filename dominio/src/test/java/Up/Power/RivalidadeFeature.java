package Up.Power; // Ou o pacote onde seus steps estão

import Up.Power.exercicio.ExercicioId;
import Up.Power.mocks.PerfilMock;
import Up.Power.mocks.RivalidadeMock;
import Up.Power.perfil.PerfilId;
import Up.Power.perfil.PerfilRepository;
import Up.Power.rivalidade.RivalidadeId;
import Up.Power.rivalidade.RivalidadeRepository;
import Up.Power.rivalidade.RivalidadeService;
import Up.Power.rivalidade.StatusRivalidade;
import io.cucumber.java.pt.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class RivalidadeFeature {

    // Repositórios "falsos" (em memória)
    private PerfilRepository perfilRepositoryMock;
    private RivalidadeRepository rivalidadeRepositoryMock;

    // O serviço que será testado
    private RivalidadeService rivalidadeService;

    // Variáveis para guardar o estado do cenário
    private Map<String, Perfil> perfis = new HashMap<>();
    private Rivalidade conviteDeRivalidade;
    private Rivalidade rivalidadeAtualizada;
    private Rivalidade rivalidadeAtiva;
    private Exception excecaoOcorrida;

    private Perfil perfilSolicitante; // Para o novo cenário
    private Perfil perfilOcupado;

    @Dado("que {string} enviou um convite de rivalidade para {string}")
    public void que_enviou_um_convite_de_rivalidade_para(String nomeUsuario1, String nomeUsuario2) {
        // 1. Inicializa os repositórios em memória e o serviço
        this.perfilRepositoryMock = new PerfilMock();
        this.rivalidadeRepositoryMock = new RivalidadeMock();
        this.rivalidadeService = new RivalidadeService(rivalidadeRepositoryMock ,perfilRepositoryMock);

        // 2. Cria os perfis e usuários de teste
        Perfil perfil1 = new Perfil(new PerfilId(1), new Email(nomeUsuario1.toLowerCase() + "@teste.com"), nomeUsuario1);
        Perfil perfil2 = new Perfil(new PerfilId(2), new Email(nomeUsuario2.toLowerCase() + "@teste.com"), nomeUsuario2);

        // Guarda os perfis em um mapa para fácil acesso nos próximos passos
        perfis.put(nomeUsuario1, perfil1);
        perfis.put(nomeUsuario2, perfil2);

        // 3. Estabelece a amizade entre eles
        Usuario amigo = new Usuario(perfil2.getUsuarioEmail(), perfil2.getUsername(), "senha123", LocalDate.now());
        perfil1.adicionarAmigo(amigo);

        // 4. Salva os perfis no nosso "banco" em memória
        perfilRepositoryMock.save(perfil1);
        perfilRepositoryMock.save(perfil2);

        // 5. Executa a ação do "Dado": envia o convite
        ExercicioId exercicioId = new ExercicioId(10);
        this.conviteDeRivalidade = rivalidadeService.enviarConviteRivalidade(perfil1.getId(), perfil2.getId(), exercicioId);

        // 6. Verificação do setup: garante que o convite foi criado corretamente
        assertNotNull(conviteDeRivalidade, "O convite de rivalidade não foi criado.");
        assertEquals(StatusRivalidade.PENDENTE, conviteDeRivalidade.getStatus(), "O convite deveria iniciar com status PENDENTE.");

        System.out.println("Dado: Convite de rivalidade enviado por '" + nomeUsuario1 + "' para '" + nomeUsuario2 + "'. Status: " + conviteDeRivalidade.getStatus());
    }

    @Dado("que o usuário possua uma rivalidade ativa")
    public void que_o_usuario_possua_uma_rivalidade_ativa() {
        // Setup inicial (igual ao outro @Dado)
        this.perfilRepositoryMock = new PerfilMock();
        this.rivalidadeRepositoryMock = new RivalidadeMock();
        this.rivalidadeService = new RivalidadeService(rivalidadeRepositoryMock, perfilRepositoryMock);

        Perfil perfil1 = new Perfil(new PerfilId(3), new Email("usuario1@teste.com"), "UsuarioUm");
        Perfil perfil2 = new Perfil(new PerfilId(4), new Email("usuario2@teste.com"), "UsuarioDois");

        perfis.put("UsuarioUm", perfil1);
        perfis.put("UsuarioDois", perfil2);

        Usuario amigo = new Usuario(perfil2.getUsuarioEmail(), perfil2.getUsername(), "senha123", LocalDate.now());
        perfil1.adicionarAmigo(amigo);
        perfilRepositoryMock.save(perfil1);
        perfilRepositoryMock.save(perfil2);

        // Ações para chegar no estado ATIVA
        // 1. Enviar convite
        ExercicioId exercicioId = new ExercicioId(11);
        Rivalidade convite = rivalidadeService.enviarConviteRivalidade(perfil1.getId(), perfil2.getId(), exercicioId);

        // 2. Aceitar convite
        this.rivalidadeAtiva = rivalidadeService.aceitarConvite(convite.getId(), perfil2.getId());

        // Verificação do setup: garante que a rivalidade está realmente ativa
        assertEquals(StatusRivalidade.ATIVA, this.rivalidadeAtiva.getStatus());
        System.out.println("Dado: Uma rivalidade ativa foi criada entre 'UsuarioUm' e 'UsuarioDois'.");
    }

    @Dado("que o usuário possua uma amizade que já está em uma rivalidade")
    public void que_o_usuario_possua_uma_amizade_que_ja_esta_em_uma_rivalidade() {
        // 1. Setup inicial (mocks e serviço)
        this.perfilRepositoryMock = new PerfilMock();
        this.rivalidadeRepositoryMock = new RivalidadeMock();
        this.rivalidadeService = new RivalidadeService(rivalidadeRepositoryMock ,perfilRepositoryMock);

        // 2. Criar 3 perfis para o cenário: A, B, C.
        // A rivalidade ativa será entre A e B. C tentará convidar B.
        Perfil perfilA = new Perfil(new PerfilId(8), new Email("perfila@teste.com"), "PerfilA");
        this.perfilOcupado = new Perfil(new PerfilId(9), new Email("perfilb@teste.com"), "PerfilB"); // B está ocupado
        this.perfilSolicitante = new Perfil(new PerfilId(15), new Email("perfilc@teste.com"), "PerfilC"); // C é o novo solicitante

        perfis.put("PerfilA", perfilA);
        perfis.put("PerfilB", perfilOcupado);
        perfis.put("PerfilC", perfilSolicitante);

        // 3. Estabelecer amizades necessárias (A-B e C-B)
        perfilA.adicionarAmigo(new Usuario(perfilOcupado.getUsuarioEmail(), "PerfilB", "s", LocalDate.now()));
        perfilSolicitante.adicionarAmigo(new Usuario(perfilOcupado.getUsuarioEmail(), "PerfilB", "s", LocalDate.now()));
        perfilRepositoryMock.save(perfilA);
        perfilRepositoryMock.save(perfilOcupado);
        perfilRepositoryMock.save(perfilSolicitante);

        // 4. Criar e ATIVAR a rivalidade pré-existente entre A e B
        ExercicioId exercicioId = new ExercicioId(99);
        Rivalidade convite = rivalidadeService.enviarConviteRivalidade(perfilA.getId(), perfilOcupado.getId(), exercicioId);
        rivalidadeService.aceitarConvite(convite.getId(), perfilOcupado.getId());

        // Verificação do setup
        assertTrue(rivalidadeRepositoryMock.existsActiveRivalryForPerfil(perfilOcupado.getId()));
        System.out.println("Dado: Perfil '" + perfilOcupado.getUsername() + "' agora está em uma rivalidade ativa.");
    }

    @Quando("um usuário optar por finalizar uma rivalidade antes do tempo")
    public void um_usuario_optar_por_finalizar_uma_rivalidade_antes_do_tempo() {
        try {
            // Pegamos um dos participantes para realizar a ação (tanto faz qual)
            Perfil perfilQueFinalizou = perfis.get("UsuarioUm");
            assertNotNull(perfilQueFinalizou, "Perfil 'UsuarioUm' não encontrado no setup.");

            // Executa a ação principal do cenário
            this.rivalidadeAtualizada = rivalidadeService.finalizarRivalidade(rivalidadeAtiva.getId(), perfilQueFinalizou.getId());

            System.out.println("Quando: 'UsuarioUm' optou por finalizar a rivalidade.");

        } catch (Exception e) {
            this.excecaoOcorrida = e;
            System.err.println("Quando: Uma exceção foi capturada ao tentar finalizar a rivalidade: " + e.getMessage());
        }
    }

    // ---------------------------------------------------------------------------------------- //

    @Quando("um usuário optar por iniciar uma rivalidade")
    public void um_usuario_optar_por_iniciar_uma_rivalidade() {
        try {
            // A ação é C (perfilSolicitante) tentando convidar B (perfilOcupado)
            ExercicioId novoExercicioId = new ExercicioId(90);
            rivalidadeService.enviarConviteRivalidade(perfilSolicitante.getId(), perfilOcupado.getId(), novoExercicioId);

        } catch (Exception e) {
            this.excecaoOcorrida = e; // Captura a exceção esperada
        }
        System.out.println("Quando: Tentativa de iniciar rivalidade com usuário ocupado.");
    }

    @Entao("o sistema informa que o usuario nao esta disponivel pois já está em uma rivalidade")
    public void o_sistema_informa_que_o_usuario_nao_esta_disponivel() {
        // 1. A verificação principal: garantir que a exceção esperada foi lançada
        assertNotNull(excecaoOcorrida, "Uma exceção deveria ter sido lançada, mas não foi.");
        assertInstanceOf(IllegalStateException.class, excecaoOcorrida, "A exceção deveria ser do tipo IllegalStateException.");

        // 2. Verificar a mensagem de erro para ter certeza que a validação correta foi acionada
        assertEquals("O usuário desafiado já está em uma rivalidade ativa.", excecaoOcorrida.getMessage());

        // 3. Garantir que nenhuma nova rivalidade foi criada
        // O cast é necessário para acessar o método findAll() do nosso mock
        long totalDeRivalidades = ((RivalidadeMock) rivalidadeRepositoryMock).findAll().size();
        assertEquals(1, totalDeRivalidades, "Nenhuma nova rivalidade deveria ter sido criada.");

        System.out.println("Então: Sistema bloqueou a criação da rivalidade com a mensagem correta: " + excecaoOcorrida.getMessage());
    }

    // ---------------------------------------------------------------------------------------- //

    @Quando("{string} aceita o convite")
    public void aceita_o_convite(String nomeUsuarioQueAceitou) {
        try {
            // Pega o perfil do usuário que está aceitando o convite
            Perfil perfilQueAceitou = perfis.get(nomeUsuarioQueAceitou);
            assertNotNull(perfilQueAceitou, "Perfil do usuário '" + nomeUsuarioQueAceitou + "' não foi encontrado no setup.");

            // Executa a ação principal do cenário: aceitar o convite
            RivalidadeId conviteId = conviteDeRivalidade.getId();
            this.rivalidadeAtualizada = rivalidadeService.aceitarConvite(conviteId, perfilQueAceitou.getId());

            System.out.println("Quando: '" + nomeUsuarioQueAceitou + "' aceitou o convite.");

        } catch (Exception e) {
            this.excecaoOcorrida = e;
            System.err.println("Quando: Uma exceção foi capturada ao tentar aceitar o convite: " + e.getMessage());
        }
    }

    @Entao("a rivalidade entre eles se torna ativa")
    public void a_rivalidade_entre_eles_se_torna_ativa() {
        // 1. Verifica se nenhuma exceção inesperada ocorreu
        assertNull(excecaoOcorrida, "Uma exceção não deveria ter sido lançada: " + (excecaoOcorrida != null ? excecaoOcorrida.getMessage() : ""));

        // 2. Verifica se o objeto foi atualizado
        assertNotNull(rivalidadeAtualizada, "A rivalidade não foi atualizada após ser aceita.");

        // 3. A verificação principal: o status foi alterado para ATIVA
        assertEquals(StatusRivalidade.ATIVA, rivalidadeAtualizada.getStatus(), "O status da rivalidade deveria ser ATIVA.");

        // 4. Verificação bônus: A data de início, que antes era nula, agora deve estar preenchida
        assertNotNull(rivalidadeAtualizada.getInicio(), "A data de início da rivalidade deveria ter sido definida.");

        System.out.println("Então: A rivalidade agora está " + rivalidadeAtualizada.getStatus() + " e começou em " + rivalidadeAtualizada.getInicio());
    }


    // ------------------------------------------------------------------------ //

    @Quando("{string} recusa o convite")
    public void recusa_o_convite(String nomeUsuarioQueRecusou) {
        try {
            // Pega o perfil do usuário que está recusando o convite
            Perfil perfilQueRecusou = perfis.get(nomeUsuarioQueRecusou);
            assertNotNull(perfilQueRecusou, "Perfil do usuário '" + nomeUsuarioQueRecusou + "' não foi encontrado no setup.");

            // Executa a ação principal do cenário: recusar o convite
            RivalidadeId conviteId = conviteDeRivalidade.getId();
            this.rivalidadeAtualizada = rivalidadeService.recusarConvite(conviteId, perfilQueRecusou.getId());

            System.out.println("Quando: '" + nomeUsuarioQueRecusou + "' recusou o convite.");

        } catch (Exception e) {
            this.excecaoOcorrida = e;
            System.err.println("Quando: Uma exceção foi capturada ao tentar recusar o convite: " + e.getMessage());
        }
    }

    @Entao("o status da rivalidade se torna {string}")
    public void o_status_da_rivalidade_se_torna(String statusEsperado) {
        // 1. Verifica se nenhuma exceção inesperada ocorreu
        assertNull(excecaoOcorrida, "Uma exceção não deveria ter sido lançada: " + (excecaoOcorrida != null ? excecaoOcorrida.getMessage() : ""));

        // 2. Converte a string do BDD para o nosso Enum
        StatusRivalidade statusEnumEsperado = StatusRivalidade.valueOf(statusEsperado.toUpperCase());

        // 3. A verificação principal: o status foi alterado para o esperado
        assertEquals(statusEnumEsperado, rivalidadeAtualizada.getStatus(), "O status da rivalidade deveria ser " + statusEsperado);

        // 4. Verificações de efeitos colaterais dependendo do status
        if (statusEnumEsperado == StatusRivalidade.RECUSADA) {
            assertNull(rivalidadeAtualizada.getInicio(), "A data de início não deveria ter sido definida para uma rivalidade recusada.");
        } else if (statusEnumEsperado == StatusRivalidade.FINALIZADA) {
            assertNotNull(rivalidadeAtualizada.getFim(), "A data de fim deveria ter sido definida para uma rivalidade finalizada.");
        }

        System.out.println("Então: O status da rivalidade é '" + rivalidadeAtualizada.getStatus() + "', como esperado.");
    }



}