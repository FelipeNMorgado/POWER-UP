package Up.Power.mocks;

import Up.Power.Email;
import Up.Power.Usuario;
import Up.Power.usuario.UsuarioRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AmizadeMock implements UsuarioRepository {

    private final Map<String, Usuario> bancoMemoria = new HashMap<>();

    @Override
    public Usuario obter(Email usuarioEmail) {
        return bancoMemoria.get(usuarioEmail.getCaracteres());
    }

    @Override
    public void salvar(Usuario usuario) {
        if (usuario == null || usuario.getUsuarioEmail() == null) {
            throw new IllegalArgumentException("Usuário inválido");
        }
        bancoMemoria.put(usuario.getUsuarioEmail().getCaracteres(), usuario);
    }

    @Override
    public int obterProximoAmizadeId() {
        return 1;
    }

    @Override
    public Usuario obterPorId(Integer id) {
        return null;
    }

    @Override
    public boolean existePorEmail(Email usuarioEmail) {
        return false;
    }

    @Override
    public boolean validarSenha(Email usuarioEmail, String senha) {
        return false;
    }

    @Override
    public void atualizar(Usuario usuario) {

    }

    @Override
    public void deletarPorId(Integer id) {

    }

    @Override
    public List<Usuario> listarTodos() {
        return List.of();
    }

    @Override
    public List<Usuario> obterPorCodigoAmizade(int codigoAmizade) {
        return List.of();
    }

    public boolean contem(Email email) {
        return bancoMemoria.containsKey(email.getCaracteres());
    }

    public int tamanho() {
        return bancoMemoria.size();
    }

    public void limpar() {
        bancoMemoria.clear();
    }
}
