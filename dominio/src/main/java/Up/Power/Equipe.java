package Up.Power;

import Up.Power.equipe.EquipeId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Equipe {
    private EquipeId id;
    private String nome;
    private String descricao;
    private String foto;
    private LocalDate inicio;
    private LocalDate fim;
    private Email usuarioAdm;
    private List<Email> usuariosEmails;
    private static final int LIMITE_MAXIMO_MEMBROS = 20;

    public Equipe(EquipeId id, String nome, Email usuarioAdm) {
        this.id = id;
        this.nome = nome;
        this.usuarioAdm = usuarioAdm;
        this.usuariosEmails = new ArrayList<>();
        this.usuariosEmails.add(usuarioAdm); // O criador é automaticamente o primeiro membro
    }

    public void adicionarMembro(Email emailUsuario) {
        if (usuariosEmails.size() >= LIMITE_MAXIMO_MEMBROS) {
            throw new IllegalStateException("A equipe atingiu o limite máximo de 20 membros");
        }
        if (!usuariosEmails.contains(emailUsuario)) {
            usuariosEmails.add(emailUsuario);
        }
    }

    public void removerMembro(Email emailUsuario) {
        if (!emailUsuario.equals(usuarioAdm)) { // Não permite remover o administrador
            usuariosEmails.remove(emailUsuario);
        }
    }

    public boolean isLider(Email emailUsuario) {
        return usuarioAdm.equals(emailUsuario);
    }

    public boolean isMembro(Email emailUsuario) {
        return usuariosEmails.contains(emailUsuario);
    }

    public void atualizarInformacoes(String nome, String descricao, String foto) {
        this.nome = nome;
        this.descricao = descricao;
        this.foto = foto;
    }

    public void definirPeriodo(LocalDate inicio, LocalDate fim) {
        this.inicio = inicio;
        this.fim = fim;
    }

    public EquipeId getId() { return id; }
    public String getNome() { return nome; }
    public String getDescricao() { return descricao; }
    public String getFoto() { return foto; }
    public LocalDate getInicio() { return inicio; }
    public LocalDate getFim() { return fim; }
    public Email getUsuarioAdm() { return usuarioAdm; }
    public List<Email> getUsuariosEmails() { return usuariosEmails; }
    public int getQuantidadeMembros() { return usuariosEmails.size(); }
    public boolean atingiuLimiteMaximo() { return usuariosEmails.size() >= LIMITE_MAXIMO_MEMBROS; }

    public void setDescricao(String descricao) { this.descricao = descricao; }
    public void setFoto(String foto) { this.foto = foto; }
    public void setInicio(LocalDate inicio) { this.inicio = inicio; }
    public void setFim(LocalDate fim) { this.fim = fim; }

}
