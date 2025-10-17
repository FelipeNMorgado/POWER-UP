package Up.Power.mocks;

import Up.Power.Email;
import Up.Power.Usuario;
import Up.Power.usuario.UsuarioRepository;

import java.util.HashMap;
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
