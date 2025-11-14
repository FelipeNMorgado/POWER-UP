package Up.Power.infraestrutura.persistencia.jpa;

import jakarta.persistence.*;
import Up.Power.Avatar;
import Up.Power.Acessorio;
import Up.Power.avatar.AvatarId;
import Up.Power.perfil.PerfilId;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "AVATAR")
class AvatarJpa {

    @Id
    private int id;

    private int perfilId; // referência ao PerfilId do domínio
    private int nivel;
    private int experiencia;
    private int dinheiro;
    private int forca;

    // Relacionamento com acessórios (opcional)
    @ManyToMany
    @JoinTable(
            name = "AVATAR_ACESSORIO",
            joinColumns = @JoinColumn(name = "avatar_id"),
            inverseJoinColumns = @JoinColumn(name = "acessorio_id")
    )
    private List<AcessorioJpa> acessorios = new ArrayList<>();

    // 🔹 Construtor padrão exigido pelo JPA
    protected AvatarJpa() {}

    // 🔹 Construtor completo usado internamente
    public AvatarJpa(int id, int perfilId, int nivel, int experiencia, int dinheiro, int forca) {
        this.id = id;
        this.perfilId = perfilId;
        this.nivel = nivel;
        this.experiencia = experiencia;
        this.dinheiro = dinheiro;
        this.forca = forca;
    }

    // =============================
    // 🔁 Conversões entre domínio e JPA
    // =============================

    // 🔹 De domínio → JPA
    public static AvatarJpa fromDomain(Avatar avatar) {
        AvatarJpa jpa = new AvatarJpa(
                Integer.parseInt(avatar.getId().toString()),
                Integer.parseInt(avatar.getPerfil().toString()),
                avatar.getNivel(),
                avatar.getExperiencia(),
                avatar.getDinheiro(),
                avatar.getForca()
        );

        // Converte acessórios do domínio para JPA (se tiver)
        if (avatar.getAcessorios() != null) {
            List<AcessorioJpa> acessoriosJpa = new ArrayList<>();
            for (Acessorio acessorio : avatar.getAcessorios()) {
                acessoriosJpa.add(AcessorioJpa.fromDomain(acessorio));
            }
            jpa.acessorios = acessoriosJpa;
        }

        return jpa;
    }

    // 🔹 De JPA → domínio
    public Avatar toDomain() {
        Avatar avatar = new Avatar(
                new AvatarId(String.valueOf(id)),
                new PerfilId(String.valueOf(perfilId))
        );

        avatar.setNivel(nivel);
        avatar.setExperiencia(experiencia);
        avatar.setForca(forca);
        // Dinheiro e acessórios
        // (só se quiser popular, depende se há getters/setters)
        return avatar;
    }

    @Override
    public String toString() {
        return "Avatar ID: " + id + " (nível " + nivel + ", força " + forca + ")";
    }
}