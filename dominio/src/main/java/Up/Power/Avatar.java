package Up.Power;

import Up.Power.avatar.AvatarId;
import Up.Power.perfil.PerfilId;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class Avatar {
    private AvatarId id;
    private PerfilId perfil;
    private List<Acessorio> acessorios;
    private Map<Integer, Boolean> acessoriosEquipados; // id -> equipado
    private int nivel;
    private int experiencia;
    private int dinheiro;
    private int forca; // Atributo adicionado

    public Avatar(AvatarId id, PerfilId perfil) {
        this.id = id;
        this.perfil = perfil;
        this.acessorios = new ArrayList<>();
        this.acessoriosEquipados = new HashMap<>();
        this.nivel = 1; // Valor padrão inicial
        this.experiencia = 0;
        this.dinheiro = 0; // Valor padrão inicial (pode ser ajustado no banco ou na criação)
        this.forca = 0;
    }

    // Getters existentes
    public AvatarId getId() { return id; }
    public PerfilId getPerfil() { return perfil; }
    public List<Acessorio> getAcessorios() { return acessorios; }
    public Map<Integer, Boolean> getAcessoriosEquipados() { return acessoriosEquipados; }
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

    public void setAcessorios(List<Acessorio> novos) {
        this.acessorios.clear();
        if (novos != null) {
            this.acessorios.addAll(novos);
        }
    }

    public void adicionarAcessorio(Acessorio acessorio, boolean equipado) {
        if (acessorio == null || acessorio.getId() == null) return;
        // garantir na lista (inventário)
        if (this.acessorios.stream().noneMatch(a -> a.getId().equals(acessorio.getId()))) {
            this.acessorios.add(acessorio);
        }
        // set flag
        this.acessoriosEquipados.put(acessorio.getId().getId(), equipado);
    }
}