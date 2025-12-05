package Up.Power;

import Up.Power.exercicio.ExercicioId;
import Up.Power.perfil.PerfilId;
import Up.Power.rivalidade.RivalidadeId;
import Up.Power.rivalidade.StatusRivalidade; // Importar o Enum

import java.time.LocalDateTime;

public class Rivalidade {
    private RivalidadeId id;
    private PerfilId perfil1;
    private PerfilId perfil2;
    private ExercicioId exercicio;
    private LocalDateTime dataConvite;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private StatusRivalidade status;

    public Rivalidade(PerfilId perfil1, PerfilId perfil2, ExercicioId exercicio) {
        this.id = null;
        this.perfil1 = perfil1;
        this.perfil2 = perfil2;
        this.exercicio = exercicio;
        this.status = StatusRivalidade.PENDENTE;
        this.dataConvite = LocalDateTime.now();
    }

    public Rivalidade(RivalidadeId id, PerfilId perfil1, PerfilId perfil2, ExercicioId exercicio, LocalDateTime dataConvite,
            LocalDateTime inicio,
            LocalDateTime fim,
            StatusRivalidade status) {
        this.id = id;
        this.perfil1 = perfil1;
        this.perfil2 = perfil2;
        this.exercicio = exercicio;
        this.dataConvite = dataConvite;
        this.inicio = inicio;
        this.fim = fim;
        this.status = status;
    }

    public void finalizar() {
        if (this.status == StatusRivalidade.ATIVA) {
            this.status = StatusRivalidade.FINALIZADA;
            this.fim = LocalDateTime.now();
        } else {
            throw new IllegalStateException("Só é possível finalizar uma rivalidade que está ATIVA.");
        }
    }

    public void recusar() {
        if (this.status == StatusRivalidade.PENDENTE) {
            this.status = StatusRivalidade.RECUSADA;
        } else {
            throw new IllegalStateException("Só é possível recusar um convite com status PENDENTE.");
        }
    }

    public void aceitar() {
        if (this.status == StatusRivalidade.PENDENTE) {
            this.status = StatusRivalidade.ATIVA;
            this.inicio = LocalDateTime.now(); // A rivalidade começa AGORA
        } else {
            throw new IllegalStateException("Só é possível aceitar um convite com status PENDENTE.");
        }
    }

    public RivalidadeId getId() { return id; }
    public PerfilId getPerfil1() { return perfil1; }
    public PerfilId getPerfil2() { return perfil2; }
    public StatusRivalidade getStatus() { return status; }
    public LocalDateTime getInicio() { return inicio; }

    public ExercicioId getExercicio() {
        return exercicio;
    }

    public LocalDateTime getDataConvite() {
        return dataConvite;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public void setId(RivalidadeId id) { this.id = id; }

}