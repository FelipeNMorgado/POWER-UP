package Up.Power;

import java.time.LocalDate;

public class Usuario {

    private Email usuarioEmail;
    private CodigoAmizade codigoAmizade;
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

    public CodigoAmizade getCodigoAmizade() {
        return codigoAmizade;
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

    public void setCodigoAmizade(CodigoAmizade codigoAmizade) {
        this.codigoAmizade = codigoAmizade;
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
