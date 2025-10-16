package Up.Power;

import Up.Power.exercicio.ExercicioId;
import Up.Power.perfil.PerfilId;

import java.time.LocalDateTime;

public class Rivalidade {
    private RivalidadeId id;
    private PerfilId perfil1;
    private PerfilId perfil2;
    private ExercicioId exercicio;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private boolean estado;

    public Rivalidade(RivalidadeId id, PerfilId perfil1, PerfilId perfil2, ExercicioId exercicio) {
        this.id = id;
        this.perfil1 = perfil1;
        this.perfil2 = perfil2;
        this.exercicio = exercicio;
        this.estado = true;
        this.inicio = LocalDateTime.now();
    }

    public RivalidadeId getId() { return id; }
    public PerfilId getPerfil1() { return perfil1; }
    public PerfilId getPerfil2() { return perfil2; }
    public ExercicioId getExercicio() { return exercicio; }
    public LocalDateTime getInicio() { return inicio; }
    public LocalDateTime getFim() { return fim; }
    public boolean isEstado() { return estado; }
}

