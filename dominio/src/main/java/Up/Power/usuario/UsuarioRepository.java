package Up.Power.usuario;

import java.util.List;

import Up.Power.Email;
import Up.Power.Usuario;

public interface UsuarioRepository {
    Usuario obter(Email usuarioEmail);
    void salvar(Usuario usuario);
      Usuario obterPorId(Integer id);
    boolean existePorEmail(Email usuarioEmail);
    boolean validarSenha(Email usuarioEmail, String senha); // ou retorna Usuario se quiser autenticar
    void atualizar(Usuario usuario); // atualiza campos persistidos
    void deletarPorId(Integer id);
    List<Usuario> listarTodos();
}


