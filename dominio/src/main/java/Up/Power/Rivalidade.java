package Up.Power;

import Up.Power.exercicio.ExercicioId;
import Up.Power.perfil.PerfilId;
import Up.Power.rivalidade.RivalidadeId;
import Up.Power.rivalidade.StatusRivalidade; // Importar o Enum

import java.time.LocalDateTime;

public class Rivalidade {
    private RivalidadeId id;
    private PerfilId perfil1; // Quem enviou o convite
    private PerfilId perfil2; // Quem recebeu o convite
    private ExercicioId exercicio;
    private LocalDateTime dataConvite;
    private LocalDateTime inicio; // Agora só será preenchido quando a rivalidade for aceita
    private LocalDateTime fim;
    private StatusRivalidade status; // <-- SUBSTITUIU o boolean 'estado'

    // Construtor para criar um NOVO CONVITE
    public Rivalidade(PerfilId perfil1, PerfilId perfil2, ExercicioId exercicio) {
        this.id = null;
        this.perfil1 = perfil1;
        this.perfil2 = perfil2;
        this.exercicio = exercicio;
        this.status = StatusRivalidade.PENDENTE; // <-- Nasce como PENDENTE
        this.dataConvite = LocalDateTime.now();
        // 'inicio' e 'fim' continuam nulos por enquanto
    }

    // Getters
    public RivalidadeId getId() { return id; }
    public PerfilId getPerfil1() { return perfil1; }
    public PerfilId getPerfil2() { return perfil2; }
    public StatusRivalidade getStatus() { return status; }
    public LocalDateTime getInicio() { return inicio; }
    // ... outros getters

    // Setters para controle de estado (usados pelo Service)
    public void setId(RivalidadeId id) { this.id = id; }

    public void aceitar() {
        if (this.status == StatusRivalidade.PENDENTE) {
            this.status = StatusRivalidade.ATIVA;
            this.inicio = LocalDateTime.now(); // A rivalidade começa AGORA
        } else {
            throw new IllegalStateException("Só é possível aceitar um convite com status PENDENTE.");
        }
    }

    public void recusar() {
        if (this.status == StatusRivalidade.PENDENTE) {
            this.status = StatusRivalidade.RECUSADA;
        } else {
            throw new IllegalStateException("Só é possível recusar um convite com status PENDENTE.");
        }
    }
}