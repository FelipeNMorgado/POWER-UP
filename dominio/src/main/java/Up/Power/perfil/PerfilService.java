package Up.Power.perfil;

import Up.Power.Email;
import Up.Power.Perfil;

import java.util.List;
import java.util.Optional;

public class PerfilService {

    private final PerfilRepository perfilRepository;

    public PerfilService(PerfilRepository perfilRepository) {
        if (perfilRepository == null) {
            throw new IllegalArgumentException("PerfilRepository é obrigatório");
        }
        this.perfilRepository = perfilRepository;
    }

    public Perfil criarPerfil(Email usuarioEmail, String username) {
        validarDadosBasicos(usuarioEmail, username);

        if (perfilRepository.existePorEmail(usuarioEmail)) {
            throw new IllegalArgumentException("Perfil para este email já existe.");
        }

        Perfil novo = new Perfil(new PerfilId(0), usuarioEmail, username);
        return perfilRepository.save(novo);
    }

    public Perfil obterPerfil(PerfilId id) {
        if (id == null) {
            throw new IllegalArgumentException("PerfilId é obrigatório");
        }
        Optional<Perfil> perfilOpt = perfilRepository.findById(id);
        if (perfilOpt.isEmpty()) {
            throw new IllegalArgumentException("Perfil não encontrado.");
        }
        return perfilOpt.get();
    }

    public Perfil obterPerfilPorId(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Id é obrigatório");
        }
        return obterPerfil(new PerfilId(id));
    }

    public Perfil obterPerfilPorEmail(Email usuarioEmail) {
        if (usuarioEmail == null) {
            throw new IllegalArgumentException("Email é obrigatório");
        }
        Optional<Perfil> perfilOpt = perfilRepository.findByUsuarioEmail(usuarioEmail.getCaracteres());
        if (perfilOpt.isEmpty()) {
            throw new IllegalArgumentException("Perfil não encontrado para o email: " + usuarioEmail.getCaracteres());
        }
        return perfilOpt.get();
    }

    public void atualizarUsername(PerfilId id, String novoUsername) {
        if (novoUsername == null || novoUsername.isBlank()) {
            throw new IllegalArgumentException("Username inválido");
        }
        Perfil perfil = obterPerfil(id);
        perfil.setUsername(novoUsername);
        perfilRepository.atualizar(perfil);
    }

    public void atualizarFoto(PerfilId id, String novaFoto) {
        Perfil perfil = obterPerfil(id);
        perfil.setFoto(novaFoto);
        perfilRepository.atualizar(perfil);
    }

    public void atualizarEstado(PerfilId id, boolean novoEstado) {
        Perfil perfil = obterPerfil(id);
        perfil.setEstado(novoEstado);
        perfilRepository.atualizar(perfil);
    }

    public boolean existe(Email usuarioEmail) {
        try {
            return perfilRepository.existePorEmail(usuarioEmail);
        } catch (AbstractMethodError | UnsupportedOperationException e) {
            return perfilRepository.findByUsuarioEmail(usuarioEmail.getCaracteres()).isPresent();
        }
    }

    public List<Perfil> listarTodos() {
        return perfilRepository.listarTodos();
    }

    public void deletarPorId(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Id é obrigatório");
        }
        perfilRepository.deletarPorId(id);
    }

    public void atualizar(Perfil perfil) {
        if (perfil == null) {
            throw new IllegalArgumentException("Perfil inválido");
        }
        perfilRepository.atualizar(perfil);
    }

    public boolean saoAmigos(PerfilId perfilId1, PerfilId perfilId2) {
        if (perfilId1 == null || perfilId2 == null) {
            return false;
        }
        return perfilRepository.existsAmizade(perfilId1, perfilId2);
    }

    public void adicionarAmizade(PerfilId perfilId1, PerfilId perfilId2) {
        if (perfilId1 == null || perfilId2 == null) {
            throw new IllegalArgumentException("PerfilIds não podem ser null");
        }
        
        if (perfilId1.equals(perfilId2)) {
            throw new IllegalArgumentException("Um perfil não pode ser amigo de si mesmo");
        }
        
        // Verificar se já são amigos
        if (saoAmigos(perfilId1, perfilId2)) {
            throw new IllegalArgumentException("Os perfis já são amigos");
        }
        
        perfilRepository.adicionarAmizade(perfilId1, perfilId2);
    }

    public void removerAmizade(PerfilId perfilId1, PerfilId perfilId2) {
        if (perfilId1 == null || perfilId2 == null) {
            throw new IllegalArgumentException("PerfilIds não podem ser null");
        }
        
        perfilRepository.removerAmizade(perfilId1, perfilId2);
    }

    public List<Perfil> listarAmigos(PerfilId perfilId) {
        if (perfilId == null) {
            throw new IllegalArgumentException("PerfilId não pode ser null");
        }
        
        return perfilRepository.listarAmigos(perfilId);
    }

    private void validarDadosBasicos(Email usuarioEmail, String username) {
        if (usuarioEmail == null || username == null || username.isBlank()) {
            throw new IllegalArgumentException("Dados obrigatórios do perfil ausentes");
        }
    }
}

