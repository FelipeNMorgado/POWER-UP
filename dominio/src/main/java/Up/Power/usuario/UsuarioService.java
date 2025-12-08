package Up.Power.usuario;

import Up.Power.AmizadeId;
import Up.Power.Email;
import Up.Power.Usuario;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class UsuarioService {

	private final UsuarioRepository usuarioRepository;

	public UsuarioService(UsuarioRepository usuarioRepository) {
		if (usuarioRepository == null) {
			throw new IllegalArgumentException("UsuarioRepository é obrigatório");
		}
		this.usuarioRepository = usuarioRepository;
	}

	public Usuario criarUsuario(Email email, String nome, String senha, LocalDate dataNascimento) {
		validarDadosBasicos(email, nome, senha);

		if (usuarioRepository.existePorEmail(email)) {
			throw new IllegalArgumentException("Usuário com este email já existe.");
		}

		Usuario novo = new Usuario(email, nome, senha, dataNascimento);
		usuarioRepository.salvar(novo);
		return novo;
	}

	public Usuario obterUsuario(Email email) {
		if (email == null) throw new IllegalArgumentException("Email é obrigatório");
		Usuario u = usuarioRepository.obter(email);
		if (u == null) throw new IllegalArgumentException("Usuário não encontrado.");
		return u;
	}

	public Usuario obterUsuarioPorId(Integer id) {
		if (id == null) throw new IllegalArgumentException("Id é obrigatório");
		Usuario u = usuarioRepository.obterPorId(id);
		if (u == null) throw new IllegalArgumentException("Usuário não encontrado.");
		return u;
	}

	public void atualizarNome(Email email, String novoNome) {
		if (novoNome == null || novoNome.isBlank()) throw new IllegalArgumentException("Nome inválido");
		Usuario u = obterUsuario(email);
		u.setNome(novoNome);
		usuarioRepository.atualizar(u);
	}

	public void atualizarSenha(Email email, String novaSenha) {
		if (novaSenha == null || novaSenha.isBlank()) throw new IllegalArgumentException("Senha inválida");
		Usuario u = obterUsuario(email);
		u.setSenha(novaSenha);
		usuarioRepository.atualizar(u);
	}

	public boolean validarSenha(Email email, String senhaInformada) {
		if (senhaInformada == null) throw new IllegalArgumentException("Senha inválida");
		// If repository offers native validation, delegate to it
		try {
			return usuarioRepository.validarSenha(email, senhaInformada);
		} catch (AbstractMethodError | UnsupportedOperationException e) {
			Usuario u = obterUsuario(email);
			return Objects.equals(u.getSenha(), senhaInformada);
		}
	}

	public void definirCodigoAmizade(Email email, AmizadeId amizadeId) {
		Usuario u = obterUsuario(email);
		u.setCodigoAmizade(amizadeId);
		usuarioRepository.atualizar(u);
	}

	public boolean existe(Email email) {
		try {
			return usuarioRepository.existePorEmail(email);
		} catch (AbstractMethodError | UnsupportedOperationException e) {
			return usuarioRepository.obter(email) != null;
		}
	}

	public List<Usuario> listarTodos() {
		return usuarioRepository.listarTodos();
	}

	public void deletarPorId(Integer id) {
		if (id == null) throw new IllegalArgumentException("Id é obrigatório");
		usuarioRepository.deletarPorId(id);
	}

	public void atualizar(Usuario usuario) {
		if (usuario == null) throw new IllegalArgumentException("Usuário inválido");
		usuarioRepository.atualizar(usuario);
	}

	private void validarDadosBasicos(Email email, String nome, String senha) {
		if (email == null || nome == null || nome.isBlank() || senha == null || senha.isBlank()) {
			throw new IllegalArgumentException("Dados obrigatórios do usuário ausentes");
		}
	}
}
