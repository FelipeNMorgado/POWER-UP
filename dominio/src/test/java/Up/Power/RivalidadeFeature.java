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
    private Exception excecaoOcorrida;

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

        // 4. Verificação bônus: A data de início deve continuar nula, pois a rivalidade nunca começou
        if (statusEnumEsperado == StatusRivalidade.RECUSADA) {
            assertNull(rivalidadeAtualizada.getInicio(), "A data de início não deveria ter sido definida para uma rivalidade recusada.");
        }

        System.out.println("Então: O status da rivalidade é '" + rivalidadeAtualizada.getStatus() + "', como esperado.");
    }



}