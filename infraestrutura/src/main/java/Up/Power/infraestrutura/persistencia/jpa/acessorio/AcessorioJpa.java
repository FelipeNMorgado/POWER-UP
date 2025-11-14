package Up.Power.infraestrutura.persistencia.jpa;

import Up.Power.acessorio.AcessorioId;
import Up.Power.Acessorio;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ACESSORIO")
public class AcessorioJpa {

    @Id
    private int id;
    private String icone;
    private int preco;
    private String nome;
    private String imagem; // 🔹 estava faltando este campo que existe no domínio

    // 🔹 Construtor padrão exigido pelo JPA
    protected AcessorioJpa() {}

    // 🔹 Construtor completo usado internamente
    public AcessorioJpa(int id, String icone, int preco, String nome, String imagem) {
        this.id = id;
        this.icone = icone;
        this.preco = preco;
        this.nome = nome;
        this.imagem = imagem;
    }

    // =============================
    // 🔁 Conversões entre domínio e JPA
    // =============================

    // 🔹 Converte de domínio (Acessorio) → entidade JPA
    public static AcessorioJpa fromDomain(Acessorio acessorio) {
        return new AcessorioJpa(
                Integer.parseInt(acessorio.getId().toString()), // ou acessorio.getId().getValor() dependendo da sua classe AcessorioId
                acessorio.getIcone(),
                acessorio.getPreco(),
                acessorio.getNome(),
                acessorio.getImagem()
        );
    }

    // 🔹 Converte de JPA → domínio
    public Acessorio toDomain() {
        return new Acessorio(
                new AcessorioId(String.valueOf(id)), // cria o id de domínio
                icone,
                preco,
                nome,
                imagem
        );
    }

    @Override
    public String toString() {
        return nome + " (R$" + preco + ")";
    }

    // Adicionar Interface aqui
}
