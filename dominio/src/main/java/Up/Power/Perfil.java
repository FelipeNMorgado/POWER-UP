package Up.Power;

import Up.Power.perfil.PerfilId;

import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class Perfil {
    private PerfilId id;
    private Email usuarioEmail;
    private List<PlanoTreino> planosTreinos;
    private PlanoNutricional planoNutricional;
    private List<Conquista> conquistas;
    private List<Meta> metas;
    private List<Usuario> amigos;
    private boolean estado;
    private LocalDateTime criacao;
    private String username;
    private String foto;
    private String conquistasSelecionadas; // IDs separados por vírgula (máximo 3)

    public Perfil(PerfilId id, Email usuarioEmail, String username) {
        this.id = id;
        this.usuarioEmail = usuarioEmail;
        this.username = username;
        this.estado = true;
        this.criacao = LocalDateTime.now();
        this.planosTreinos = new ArrayList<>();
        this.conquistas = new ArrayList<>();
        this.metas = new ArrayList<>();
        this.amigos = new ArrayList<>();
    }

    public void adicionarPlanoTreino(PlanoTreino plano) {
        this.planosTreinos.add(plano);
    }

    public void removerPlanoTreino(PlanoTreino plano) {
        this.planosTreinos.remove(plano);
    }

    public PerfilId getId() {
        return id;
    }

    public Email getUsuarioEmail() {
        return usuarioEmail;
    }

    public List<PlanoTreino> getPlanosTreinos() {
        return planosTreinos;
    }

    public PlanoNutricional getPlanoNutricional() {
        return planoNutricional;
    }

    public List<Conquista> getConquistas() {
        return conquistas;
    }

    public List<Meta> getMetas() {
        return metas;
    }

    public List<Usuario> getAmigos() {
        return amigos;
    }

    public boolean isEstado() {
        return estado;
    }

    public LocalDateTime getCriacao() {
        return criacao;
    }

    public String getUsername() {
        return username;
    }

    public String getFoto() {
        return foto;
    }

    public String getConquistasSelecionadas() {
        return conquistasSelecionadas;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void setConquistasSelecionadas(String conquistasSelecionadas) {
        // Validar que não há mais de 3 conquistas
        if (conquistasSelecionadas != null && !conquistasSelecionadas.isEmpty()) {
            String[] ids = conquistasSelecionadas.split(",");
            if (ids.length > 3) {
                throw new IllegalArgumentException("Máximo de 3 conquistas podem ser selecionadas");
            }
        }
        this.conquistasSelecionadas = conquistasSelecionadas;
    }

    // Operações do agregado (idempotentes)
    public void definirPlanoNutricional(PlanoNutricional plano) {
        this.planoNutricional = plano;
    }

    public void adicionarConquista(Conquista c) {
        if (c != null && !conquistas.contains(c)) conquistas.add(c);
    }

    public void removerConquista(Conquista c) {
        conquistas.remove(c);
    }

    public void adicionarMeta(Meta m) {
        if (m != null && !metas.contains(m)) metas.add(m);
    }

    public void removerMeta(Meta m) {
        metas.remove(m);
    }

    public void adicionarAmigo(Usuario u) {
        if (u != null && !amigos.contains(u)) amigos.add(u);
    }

    public void removerAmigo(Usuario u) {
        amigos.remove(u);
    }
}


