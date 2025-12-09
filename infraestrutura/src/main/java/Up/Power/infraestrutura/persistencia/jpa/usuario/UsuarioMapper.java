package Up.Power.infraestrutura.persistencia.jpa.usuario;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.stereotype.Component;

import Up.Power.AmizadeId;
import Up.Power.Email;
import Up.Power.Usuario;

@Component
public class UsuarioMapper {

    public Date toDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    private LocalDate toLocalDate(Date date) {
        if (date == null) {
            return null;
        }
        // java.sql.Date não suporta toInstant(), então usamos toLocalDate() diretamente
        if (date instanceof java.sql.Date) {
            return ((java.sql.Date) date).toLocalDate();
        }
        // Para java.util.Date, usa toInstant()
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public UsuarioJpa toEntity(Usuario domain) {
        UsuarioJpa entity = new UsuarioJpa();

        entity.setUsuarioEmail(domain.getUsuarioEmail().getCaracteres());

        if (domain.getCodigoAmizade() != null)
            entity.setAmizadeId(domain.getCodigoAmizade().getCodigo());

        entity.setNome(domain.getNome());
        entity.setSenha(domain.getSenha());
        entity.setDataNascimento(toDate(domain.getDataNascimento()));

        return entity;
    }

    public Usuario toDomain(UsuarioJpa entity) {
        Usuario usuario = new Usuario(
            new Email(entity.getUsuarioEmail()),
            entity.getNome(),
            entity.getSenha(),
            toLocalDate(entity.getDataNascimento())
        );

        if (entity.getAmizadeId() != null)
            usuario.setCodigoAmizade(new AmizadeId(entity.getAmizadeId()));

        return usuario;
    }
}
