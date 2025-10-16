package Up.Power;

import Up.Power.avatar.AvatarId;
import Up.Power.perfil.PerfilId;

import java.util.ArrayList;
import java.util.List;

public class Avatar {
    private AvatarId id;
    private PerfilId perfil;
    private List<Acessorio> acessorios;
    private int nivel;
    private int experiencia;
    private int dinheiro;

    public Avatar(AvatarId id, PerfilId perfil) {
        this.id = id;
        this.perfil = perfil;
        this.acessorios = new ArrayList<>();
    }

    public AvatarId getId() { return id; }
    public PerfilId getPerfil() { return perfil; }
    public List<Acessorio> getAcessorios() { return acessorios; }
    public int getNivel() { return nivel; }
    public int getExperiencia() { return experiencia; }
    public int getDinheiro() { return dinheiro; }
}


