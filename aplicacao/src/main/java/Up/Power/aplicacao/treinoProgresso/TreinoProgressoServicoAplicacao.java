package Up.Power.aplicacao.treinoProgresso;

import Up.Power.exercicio.ExercicioId;
import Up.Power.perfil.PerfilId;
import Up.Power.treinoProgresso.TreinoProgresso;
import Up.Power.treinoProgresso.TreinoProgressoId;
import Up.Power.treinoProgresso.TreinoProgressoRepository;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class TreinoProgressoServicoAplicacao {

    private final TreinoProgressoRepository repository;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    public TreinoProgressoServicoAplicacao(TreinoProgressoRepository repository) {
        this.repository = repository;
    }

    public List<TreinoProgressoResumo> listar(Integer perfilId, Integer exercicioId) {
        if (perfilId == null) {
            throw new IllegalArgumentException("perfilId é obrigatório");
        }
        var perfil = new PerfilId(perfilId);
        List<TreinoProgresso> resultados;
        if (exercicioId != null) {
            resultados = repository.listarPorPerfilEExercicio(perfil, new ExercicioId(exercicioId));
        } else {
            resultados = repository.listarPorPerfil(perfil);
        }
        return resultados.stream().map(this::toResumo).toList();
    }

    public TreinoProgressoResumo registrar(RegistrarTreinoProgressoCommand command) {
        if (command.perfilId() == null || command.exercicioId() == null || command.dataRegistro() == null) {
            throw new IllegalArgumentException("perfilId, exercicioId e dataRegistro são obrigatórios");
        }
        Date data;
        try {
            data = dateFormat.parse(command.dataRegistro());
        } catch (ParseException e) {
            throw new IllegalArgumentException("dataRegistro deve estar no formato yyyy-MM-dd");
        }

        TreinoProgresso progresso = new TreinoProgresso(
                new TreinoProgressoId(null),
                new PerfilId(command.perfilId()),
                new ExercicioId(command.exercicioId()),
                data,
                command.pesoKg(),
                command.repeticoes(),
                command.series(),
                new Date()
        );

        TreinoProgresso salvo = repository.salvar(progresso);
        return toResumo(salvo);
    }

    public void deletar(Integer id) {
        repository.deletar(new TreinoProgressoId(id));
    }

    private TreinoProgressoResumo toResumo(TreinoProgresso t) {
        return new TreinoProgressoResumo(
                t.getId() != null ? t.getId().getId() : null,
                t.getPerfilId().getId(),
                t.getExercicioId().getId(),
                t.getDataRegistro(),
                t.getPesoKg(),
                t.getRepeticoes(),
                t.getSeries(),
                t.getCreatedAt()
        );
    }
}

