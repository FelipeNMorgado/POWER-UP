package Up.Power;

import Up.Power.exercicio.ExercicioId;
import Up.Power.mocks.PlanoTreinoMock;
import Up.Power.planoTreino.Dias;
import Up.Power.planoTreino.PlanoTId;
import Up.Power.planoTreino.PlanoTreinoRepository;
import Up.Power.treino.TipoTreino;
import io.cucumber.java.pt.*;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TreinoFeature {

    private PlanoTreinoRepository planoRepo;
    private PlanoTreino plano;
    private Treino treino;
    private String mensagem;

    // ------------------------------------------------------
    // Cenário: Criar rotina de treino
    // ------------------------------------------------------

    @Quando("criar uma rotina e adicionar exercícios do banco pré-existentes")
    public void criar_rotina_e_adicionar_exercicios() {
        planoRepo = new PlanoTreinoMock();
        ((PlanoTreinoMock) planoRepo).limpar();
        plano = new PlanoTreino(new PlanoTId(1), new Email("u@x.com"), "Rotina A");
        treino = new Treino(new Up.Power.treino.TreinoId(10), new ExercicioId(100), TipoTreino.Peso, 10, 50f, 3, 60);
        plano.adicionarTreino(treino);
        plano.adicionarDia(Dias.Segunda);
        planoRepo.salvar(plano);
        mensagem = "Plano salvo";
    }

    @Entao("a rotina é salva com séries, repetições, carga e descanso definidos por ele")
    public void rotina_salva_com_parametros() {
        assertTrue(((PlanoTreinoMock) planoRepo).obter(new PlanoTId(1)).isPresent());
        Treino salvo = plano.getTreinos().get(0);
        assertEquals(10, salvo.getRepeticoes());
        assertEquals(50f, salvo.getPeso());
        assertEquals(3, salvo.getSeries());
        assertEquals("Plano salvo", mensagem);
        // Limpeza via exclusão explícita
        ((PlanoTreinoMock) planoRepo).excluir(new PlanoTId(1));
    }

    // ------------------------------------------------------
    // Cenário: Validar plano mínimo
    // ------------------------------------------------------

    @Dado("criação de uma rotina")
    public void criacao_de_uma_rotina() {
        planoRepo = new PlanoTreinoMock();
    // Garante estado limpo antes do cenário
        ((PlanoTreinoMock) planoRepo).limpar();
    // Cria um PlanoTreino vazio (sem exercícios) para validar a regra mínima
        plano = new PlanoTreino(new PlanoTId(2), new Email("u@x.com"), "Rotina B");
    }

    @Quando("o plano não tiver ao menos um exercício definido")
    public void plano_sem_exercicio() {
        try {
        // Tenta salvar um plano sem nenhum treino; o mock deve lançar exceção
            planoRepo.salvar(plano);
            mensagem = "Salvou";
        } catch (Exception e) {
        // Captura a mensagem de erro para validação no passo "Então"
            mensagem = e.getMessage();
        }
    }

    @Entao("o sistema bloqueia o salvamento e informa o requisito mínimo")
    public void bloqueia_salvamento_requisito_minimo() {
    // Verifica que a mensagem informa a exigência de ao menos um exercício
        assertTrue(mensagem != null && mensagem.contains("ao menos um exercício"));
    // Garante que o plano não foi persistido no repositório em memória
        assertTrue(planoRepo.listar(new PlanoTId(2)).isEmpty());
    }

    // ------------------------------------------------------
    // Cenário: Editar rotina
    // ------------------------------------------------------

    @Dado("uma rotina já criada")
    public void rotina_ja_criada() {
        planoRepo = new PlanoTreinoMock();
        ((PlanoTreinoMock) planoRepo).limpar();
        plano = new PlanoTreino(new PlanoTId(3), new Email("u@x.com"), "Rotina C");
        Treino t = new Treino(new Up.Power.treino.TreinoId(11), new ExercicioId(101), TipoTreino.Peso, 8, 40f, 3, 60);
        plano.adicionarTreino(t);
        planoRepo.salvar(plano);
    }

    @Quando("alterar séries, repetições, carga, descanso ou exercício")
    public void alterar_parametros_ou_exercicio() {
        Treino t = plano.getTreinos().get(0);
        t.atualizarParametros(12, 55f, 4, 90);
        t.atualizarExercicio(new ExercicioId(102));
        planoRepo.salvar(plano);
    }

    @Entao("o sistema atualiza a rotina com os novos parâmetros")
    public void sistema_atualiza_rotina() {
        Treino t = plano.getTreinos().get(0);
        assertEquals(12, t.getRepeticoes());
        assertEquals(55f, t.getPeso());
        assertEquals(4, t.getSeries());
        assertEquals(102, t.getExercicio().getId());
        ((PlanoTreinoMock) planoRepo).excluir(new PlanoTId(3));
    }

    // ------------------------------------------------------
    // Cenário: Visualizar rotinas de treino
    // ------------------------------------------------------

    @Dado("que o usuário possua rotinas cadastradas")
    public void usuario_possui_rotinas() {
        planoRepo = new PlanoTreinoMock();
        ((PlanoTreinoMock) planoRepo).limpar();
        plano = new PlanoTreino(new PlanoTId(4), new Email("u@x.com"), "Rotina D");
        plano.adicionarTreino(new Treino(new Up.Power.treino.TreinoId(12), new ExercicioId(103), TipoTreino.Peso, 6, 30f, 3, 60));
        planoRepo.salvar(plano);
    }

    @Quando("acessar a aba de treinos")
    public void acessar_aba_treinos() {
        // sem ação extra, apenas consulta
    }

    @Entao("o sistema lista as rotinas e exibe os detalhes de cada exercício \\(carga, repetição e descanso)")
    public void o_sistema_lista_as_rotinas_e_exibe_os_detalhes_de_cada_exercício_carga_repetição_e_descanso() {
        assertFalse(planoRepo.listar(null).isEmpty());
        Treino t = plano.getTreinos().get(0);
        assertEquals(30f, t.getPeso());
        assertEquals(6, t.getRepeticoes());
        assertEquals(3, t.getSeries());
        ((PlanoTreinoMock) planoRepo).excluir(new PlanoTId(4));
    }
}


