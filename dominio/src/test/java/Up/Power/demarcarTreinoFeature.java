package Up.Power;

import Up.Power.frequencia.*;
import Up.Power.mocks.MockFrequencia;
import Up.Power.perfil.PerfilId;
import Up.Power.planoTreino.Dias;
import Up.Power.planoTreino.PlanoTId;
import Up.Power.treino.TreinoId;
import io.cucumber.java.pt.*;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class demarcarTreinoFeature {

    private FrequenciaRepository repositoryMock;
    private FrequenciaService frequenciaService;
    private PerfilId perfilId;
    private FrequenciaId frequenciaId;
    private TreinoId treinoId;
    private PlanoTreino planoTreino;

    @Dado("que o sistema possua um usuário criado")
    public void que_o_sistema_possua_um_usuario_criado() {
        perfilId = new PerfilId(1);
        frequenciaId = new FrequenciaId(1);
        treinoId = new TreinoId(1);

        repositoryMock = new MockFrequencia(List.of());
        frequenciaService = new FrequenciaService(repositoryMock);

        planoTreino = new PlanoTreino(
                new PlanoTId(1),
                new Email("usuario@teste.com"),
                "Plano de Treino Semanal"
        );

        System.out.println("Usuário e plano de treino criados com sucesso.");
    }


    @Quando("o usuário tentar escolher os dias que vai à academia")
    public void o_usuario_tentar_escolher_os_dias_que_vai_a_academia() {
        planoTreino.adicionarDia(Dias.Segunda);
        planoTreino.adicionarDia(Dias.Quarta);
        planoTreino.adicionarDia(Dias.Sexta);

        System.out.println("Dias escolhidos: " + planoTreino.getDias());
    }

    @Entao("o sistema informa os dias disponíveis para sua frequência")
    public void o_sistema_informa_os_dias_disponiveis_para_sua_frequencia() {
        List<Dias> diasDoPlano = planoTreino.getDias();

        assertFalse(diasDoPlano.isEmpty());
        assertEquals(3, diasDoPlano.size());
        assertTrue(diasDoPlano.contains(Dias.Segunda));
        assertTrue(diasDoPlano.contains(Dias.Quarta));
        assertTrue(diasDoPlano.contains(Dias.Sexta));

        System.out.println("Dias de treino registrados no plano: " + diasDoPlano);
    }

    @Quando("o usuário tentar demarcar sua frequência")
    public void o_usuario_tentar_demarcar_sua_frequencia() {
        frequenciaService.registrarPresenca(frequenciaId, perfilId, treinoId);
        System.out.println("Frequência registrada!");
    }

    @Entao("o sistema demarca que o usuário foi à academia")
    public void o_sistema_demarcar_que_o_usuario_foi_a_academia() {
        boolean presente = frequenciaService.usuarioFoiAoTreino(frequenciaId);
        assertTrue(presente);
        System.out.println("Presença confirmada.");
    }

    @E("adiciona um dia a mais na contagem de treinos da semana")
    public void adiciona_um_dia_a_mais_na_contagem_de_treinos_da_semana() {
        assertTrue(frequenciaService.getContagemSemanal() > 0);
        System.out.println("Contagem atual de treinos: " + frequenciaService.getContagemSemanal());
    }

    @Quando("o usuário não marcar presença em um dia específico")
    public void o_usuario_nao_marcar_presenca_em_um_dia_especifico() {
        frequenciaService.registrarAusencia(frequenciaId);
        System.out.println("Usuário não marcou presença hoje.");
    }

    @Entao("o sistema informa que o usuário não foi à academia")
    public void o_sistema_informa_que_o_usuario_nao_foi_a_academia() {
        boolean presente = frequenciaService.usuarioFoiAoTreino(frequenciaId);
        assertFalse(presente);
        System.out.println("Usuário ausente.");
    }

    @E("zera a contagem de treinos em sequência")
    public void zera_a_contagem_de_treinos_em_sequencia() {
        assertEquals(0, frequenciaService.getContagemSemanal());
        System.out.println("Contagem zerada.");
    }
}
