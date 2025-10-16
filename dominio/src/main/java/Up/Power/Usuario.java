package Up.Power;

import java.time.LocalDate;

public class Usuario {

    private Email usuarioEmail;
    private AmizadeId amizadeId;
    private String nome;
    private String senha;
    private java.time.LocalDate dataNascimento;

    public Usuario (Email usuarioEmail, String nome, String senha, LocalDate dataNascimento) {
        this.usuarioEmail = usuarioEmail;
        this.nome = nome;
        this.senha = senha;
        this.dataNascimento = dataNascimento;
    }

    public Email getUsuarioEmail() {
        return usuarioEmail;
    }

    public AmizadeId getCodigoAmizade() {
        return amizadeId;
    }

    public String getNome() {
        return nome;
    }

    public String getSenha() {
        return senha;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setCodigoAmizade(AmizadeId amizadeId) {
        this.amizadeId = amizadeId;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }
}
