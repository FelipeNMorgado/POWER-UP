package Up.Power.perfil;

import Up.Power.Perfil;
import Up.Power.Email;

import java.util.List;
import java.util.Optional;

public interface PerfilRepository {
    Optional<Perfil> findById(PerfilId id);
    Perfil save(Perfil perfil);
    boolean existsAmizade(PerfilId perfilId1, PerfilId perfilId2);
    Optional<Perfil> findByUsuarioEmail(String usuarioEmail);
    List<Perfil> listarTodos();
    void deletarPorId(Integer id);
    boolean existePorEmail(Email usuarioEmail);
    void atualizar(Perfil perfil);
    
    // Métodos de amizade
    void adicionarAmizade(PerfilId perfilId1, PerfilId perfilId2);
    void removerAmizade(PerfilId perfilId1, PerfilId perfilId2);
    List<Perfil> listarAmigos(PerfilId perfilId);
}


