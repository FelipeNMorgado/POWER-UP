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
import io.cucumber.java.en.*;
import io.cucumber.java.pt.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class RivalidadeFeature {

    private PerfilRepository perfilRepositoryMock;
    private RivalidadeRepository rivalidadeRepositoryMock;

    private RivalidadeService rivalidadeService;

    private Map<String, Perfil> perfis = new HashMap<>();
    private Rivalidade conviteDeRivalidade;
    private Rivalidade rivalidadeAtualizada;
    private Rivalidade rivalidadeAtiva;
    private Exception excecaoOcorrida;


    @Given("que {string} enviou um convite de rivalidade para {string}")
    public void que_enviou_um_convite_de_rivalidade_para(String nomeUsuario1, String nomeUsuario2) {
        this.perfilRepositoryMock = new PerfilMock();
        this.rivalidadeRepositoryMock = new RivalidadeMock();
        this.rivalidadeService = new RivalidadeService(rivalidadeRepositoryMock ,perfilRepositoryMock);

        Perfil perfil1 = new Perfil(new PerfilId(1), new Email(nomeUsuario1.toLowerCase() + "@teste.com"), nomeUsuario1);
        Perfil perfil2 = new Perfil(new PerfilId(2), new Email(nomeUsuario2.toLowerCase() + "@teste.com"), nomeUsuario2);
        perfis.put(nomeUsuario1, perfil1);
        perfis.put(nomeUsuario2, perfil2);

        Usuario amigo = new Usuario(perfil2.getUsuarioEmail(), perfil2.getUsername(), "senha123", LocalDate.now());
        perfil1.adicionarAmigo(amigo);

        perfilRepositoryMock.save(perfil1);
        perfilRepositoryMock.save(perfil2);

        ExercicioId exercicioId = new ExercicioId(10);
        this.conviteDeRivalidade = rivalidadeService.enviarConviteRivalidade(perfil1.getId(), perfil2.getId(), exercicioId);

        assertNotNull(conviteDeRivalidade, "O convite de rivalidade não foi criado.");
        assertEquals(StatusRivalidade.PENDENTE, conviteDeRivalidade.getStatus(), "O convite deveria iniciar com status PENDENTE.");

        System.out.println("Dado: Convite de rivalidade enviado por '" + nomeUsuario1 + "' para '" + nomeUsuario2 + "'. Status: " + conviteDeRivalidade.getStatus());
    }

    @When("{string} aceita o convite")
    public void aceita_o_convite(String nomeUsuarioQueAceitou) {
        try {
            Perfil perfilQueAceitou = perfis.get(nomeUsuarioQueAceitou);
            assertNotNull(perfilQueAceitou, "Perfil do usuário '" + nomeUsuarioQueAceitou + "' não foi encontrado no setup.");

            RivalidadeId conviteId = conviteDeRivalidade.getId();
            this.rivalidadeAtualizada = rivalidadeService.aceitarConvite(conviteId, perfilQueAceitou.getId());

            System.out.println("Quando: '" + nomeUsuarioQueAceitou + "' aceitou o convite.");

        } catch (Exception e) {
            this.excecaoOcorrida = e;
            System.err.println("Quando: Uma exceção foi capturada ao tentar aceitar o convite: " + e.getMessage());
        }
    }

    @Then("a rivalidade entre eles se torna ativa")
    public void a_rivalidade_entre_eles_se_torna_ativa() {
        assertNull(excecaoOcorrida, "Uma exceção não deveria ter sido lançada: " + (excecaoOcorrida != null ? excecaoOcorrida.getMessage() : ""));

        assertNotNull(rivalidadeAtualizada, "A rivalidade não foi atualizada após ser aceita.");

        assertEquals(StatusRivalidade.ATIVA, rivalidadeAtualizada.getStatus(), "O status da rivalidade deveria ser ATIVA.");

        assertNotNull(rivalidadeAtualizada.getInicio(), "A data de início da rivalidade deveria ter sido definida.");

        System.out.println("Então: A rivalidade agora está " + rivalidadeAtualizada.getStatus() + " e começou em " + rivalidadeAtualizada.getInicio());
    }

    // ---------------Finalizar uma rivalidade--------------- //

    @Given("que o usuário possua uma rivalidade ativa")
    public void que_o_usuario_possua_uma_rivalidade_ativa() {
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

        ExercicioId exercicioId = new ExercicioId(11);
        Rivalidade convite = rivalidadeService.enviarConviteRivalidade(perfil1.getId(), perfil2.getId(), exercicioId);

        this.rivalidadeAtiva = rivalidadeService.aceitarConvite(convite.getId(), perfil2.getId());

        assertEquals(StatusRivalidade.ATIVA, this.rivalidadeAtiva.getStatus());
        System.out.println("Dado: Uma rivalidade ativa foi criada entre 'UsuarioUm' e 'UsuarioDois'.");
    }

    @When("um usuário optar por finalizar uma rivalidade antes do tempo")
    public void um_usuario_optar_por_finalizar_uma_rivalidade_antes_do_tempo() {
        try {
            Perfil perfilQueFinalizou = perfis.get("UsuarioUm");
            assertNotNull(perfilQueFinalizou, "Perfil 'UsuarioUm' não encontrado no setup.");

            this.rivalidadeAtualizada = rivalidadeService.finalizarRivalidade(rivalidadeAtiva.getId(), perfilQueFinalizou.getId());

            System.out.println("Quando: 'UsuarioUm' optou por finalizar a rivalidade.");

        } catch (Exception e) {
            this.excecaoOcorrida = e;
            System.err.println("Quando: Uma exceção foi capturada ao tentar finalizar a rivalidade: " + e.getMessage());
        }
    }

    // ------------------------------------------------------ //

    // ------------------------Amigo ja esta em uma rivalidade--------------------------------- //

    @Given("que o usuário possua uma amizade que já está em uma rivalidade")
    public void que_o_usuario_possua_uma_amizade_que_ja_esta_em_uma_rivalidade() {
        this.perfilRepositoryMock = new PerfilMock();
        this.rivalidadeRepositoryMock = new RivalidadeMock();
        this.rivalidadeService = new RivalidadeService(rivalidadeRepositoryMock ,perfilRepositoryMock);

        Perfil perfilA = new Perfil(new PerfilId(8), new Email("perfila@teste.com"), "PerfilA");
        Perfil perfilOcupado = new Perfil(new PerfilId(9), new Email("perfilb@teste.com"), "PerfilB"); // B está ocupado
        Perfil perfilSolicitante = new Perfil(new PerfilId(15), new Email("perfilc@teste.com"), "PerfilC"); // C é o novo solicitante

        perfis.put("PerfilA", perfilA);
        perfis.put("PerfilB", perfilOcupado);
        perfis.put("PerfilC", perfilSolicitante);

        perfilA.adicionarAmigo(new Usuario(perfilOcupado.getUsuarioEmail(), "PerfilB", "s", LocalDate.now()));
        perfilSolicitante.adicionarAmigo(new Usuario(perfilOcupado.getUsuarioEmail(), "PerfilB", "s", LocalDate.now()));
        perfilRepositoryMock.save(perfilA);
        perfilRepositoryMock.save(perfilOcupado);
        perfilRepositoryMock.save(perfilSolicitante);

        ExercicioId exercicioId = new ExercicioId(99);
        Rivalidade convite = rivalidadeService.enviarConviteRivalidade(perfilA.getId(), perfilOcupado.getId(), exercicioId);
        rivalidadeService.aceitarConvite(convite.getId(), perfilOcupado.getId());

        assertTrue(rivalidadeRepositoryMock.existsActiveRivalryForPerfil(perfilOcupado.getId()));
        System.out.println("Dado: Perfil '" + perfilOcupado.getUsername() + "' agora está em uma rivalidade ativa.");
    }


    @When("um usuário optar por iniciar uma rivalidade")
    public void um_usuario_optar_por_iniciar_uma_rivalidade() {
        try {
            ExercicioId novoExercicioId = new ExercicioId(90);
            Perfil perfilSolicitante = perfis.get("PerfilC");
            Perfil perfilOcupado = perfis.get("PerfilB");
            rivalidadeService.enviarConviteRivalidade(perfilSolicitante.getId(), perfilOcupado.getId(), novoExercicioId);

        } catch (Exception e) {
            this.excecaoOcorrida = e; // Captura a exceção esperada
        }
        System.out.println("Quando: Tentativa de iniciar rivalidade com usuário ocupado.");
    }

    @Then("o sistema informa que o usuario nao esta disponivel pois já está em uma rivalidade")
    public void o_sistema_informa_que_o_usuario_nao_esta_disponivel() {
        assertNotNull(excecaoOcorrida, "Uma exceção deveria ter sido lançada, mas não foi.");
        assertInstanceOf(IllegalStateException.class, excecaoOcorrida, "A exceção deveria ser do tipo IllegalStateException.");

        assertEquals("O usuário desafiado já está em uma rivalidade ativa.", excecaoOcorrida.getMessage());

        long totalDeRivalidades = ((RivalidadeMock) rivalidadeRepositoryMock).findAll().size();
        assertEquals(1, totalDeRivalidades, "Nenhuma nova rivalidade deveria ter sido criada.");

        System.out.println("Então: Sistema bloqueou a criação da rivalidade com a mensagem correta: " + excecaoOcorrida.getMessage());
    }

    // ---------------------------------------------------------------------------------------- //

    // ----------------------Dado que o convite e enviado mas recusado--------------------------- //

    @When("{string} recusa o convite")
    public void recusa_o_convite(String nomeUsuarioQueRecusou) {
        try {
            Perfil perfilQueRecusou = perfis.get(nomeUsuarioQueRecusou);
            assertNotNull(perfilQueRecusou, "Perfil do usuário '" + nomeUsuarioQueRecusou + "' não foi encontrado no setup.");

            RivalidadeId conviteId = conviteDeRivalidade.getId();
            this.rivalidadeAtualizada = rivalidadeService.recusarConvite(conviteId, perfilQueRecusou.getId());

            System.out.println("Quando: '" + nomeUsuarioQueRecusou + "' recusou o convite.");

        } catch (Exception e) {
            this.excecaoOcorrida = e;
            System.err.println("Quando: Uma exceção foi capturada ao tentar recusar o convite: " + e.getMessage());
        }
    }

    // -----------------------Finalizar duelo antes da hora ou recusar--------------------------- //
    @Then("o status da rivalidade se torna {string}")
    public void o_status_da_rivalidade_se_torna(String statusEsperado) {
        assertNull(excecaoOcorrida, "Uma exceção não deveria ter sido lançada: " + (excecaoOcorrida != null ? excecaoOcorrida.getMessage() : ""));

        StatusRivalidade statusEnumEsperado = StatusRivalidade.valueOf(statusEsperado.toUpperCase());

        assertEquals(statusEnumEsperado, rivalidadeAtualizada.getStatus(), "O status da rivalidade deveria ser " + statusEsperado);

        if (statusEnumEsperado == StatusRivalidade.RECUSADA) {
            assertNull(rivalidadeAtualizada.getInicio(), "A data de início não deveria ter sido definida para uma rivalidade recusada.");
        } else if (statusEnumEsperado == StatusRivalidade.FINALIZADA) {
            assertNotNull(rivalidadeAtualizada.getFim(), "A data de fim deveria ter sido definida para uma rivalidade finalizada.");
        }

        System.out.println("Então: O status da rivalidade é '" + rivalidadeAtualizada.getStatus() + "', como esperado.");
    }

}