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
    private int forca; // Atributo adicionado

    public Avatar(AvatarId id, PerfilId perfil) {
        this.id = id;
        this.perfil = perfil;
        this.acessorios = new ArrayList<>();
        this.nivel = 1; // Valor padrão inicial
    }

    // Getters existentes
    public AvatarId getId() { return id; }
    public PerfilId getPerfil() { return perfil; }
    public List<Acessorio> getAcessorios() { return acessorios; }
    public int getNivel() { return nivel; }
    public int getExperiencia() { return experiencia; }
    public int getDinheiro() { return dinheiro; }
    public int getForca() { return forca; } // Getter adicionado

    // Setters existentes
    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public void setExperiencia(int experiencia) {
        this.experiencia = experiencia;
    }

    public void setPerfil(PerfilId perfil) {
        this.perfil = perfil;
    }

    public void setForca(int forca) { // Setter adicionado
        this.forca = forca;
    }

    public void setDinheiro(int dinheiro) {
        this.dinheiro = dinheiro;
    }
}