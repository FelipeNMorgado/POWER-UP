package Up.Power.usuario;

import java.util.List;

import Up.Power.Email;
import Up.Power.Usuario;

public interface UsuarioRepository {
    Usuario obter(Email usuarioEmail);
    void salvar(Usuario usuario);
<<<<<<< HEAD
    int obterProximoAmizadeId();
=======
      Usuario obterPorId(Integer id);
    boolean existePorEmail(Email usuarioEmail);
    boolean validarSenha(Email usuarioEmail, String senha); // ou retorna Usuario se quiser autenticar
    void atualizar(Usuario usuario); // atualiza campos persistidos
    void deletarPorId(Integer id);
    List<Usuario> listarTodos();
>>>>>>> f77d28b87b6f53cf6500eb270b7b86d3e980f714
}


