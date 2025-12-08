package Up.Power.aplicacao.usuario;

import java.util.List;

public interface UsuarioRepositorioAplicacao {

    UsuarioResumo obterPorEmail(String email);

    List<UsuarioResumo> listarAmigos(String email);

}
