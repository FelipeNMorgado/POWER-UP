package Up.Power.rivalidade;


import Up.Power.Rivalidade;
import Up.Power.exercicio.ExercicioId;
import Up.Power.perfil.PerfilId;
import Up.Power.perfil.PerfilRepository;

public class RivalidadeService {

    private final RivalidadeRepository rivalidadeRepository;
    private final PerfilRepository perfilRepository;

    public RivalidadeService(RivalidadeRepository rivalidadeRepository, PerfilRepository perfilRepository) {
        this.rivalidadeRepository = rivalidadeRepository;
        this.perfilRepository = perfilRepository;
    }

    public Rivalidade enviarConviteRivalidade(PerfilId idPerfil1, PerfilId idPerfil2, ExercicioId exercicioId) {
        if (rivalidadeRepository.existsActiveRivalryForPerfil(idPerfil1)) {
            throw new IllegalStateException("O solicitante já está em uma rivalidade ativa.");
        }
        if (rivalidadeRepository.existsActiveRivalryForPerfil(idPerfil2)) {
            throw new IllegalStateException("O usuário desafiado já está em uma rivalidade ativa.");
        }



        // Cria uma rivalidade com o status PENDENTE
        Rivalidade novoConvite = new Rivalidade(idPerfil1, idPerfil2, exercicioId);
        return rivalidadeRepository.save(novoConvite);
    }

    // Metodo 2: Aceitar um convite
    public Rivalidade aceitarConvite(RivalidadeId rivalidadeId, PerfilId idUsuarioQueAceitou) {
        // Busca a rivalidade no banco
        Rivalidade rivalidade = rivalidadeRepository.findById(rivalidadeId)
                .orElseThrow(() -> new IllegalArgumentException("Rivalidade não encontrada."));

        // VERIFICAÇÃO DE SEGURANÇA: Garante que a pessoa aceitando é a pessoa convidada (perfil2)
        if (!rivalidade.getPerfil2().equals(idUsuarioQueAceitou)) {
            throw new SecurityException("Usuário não autorizado a aceitar este convite.");
        }

        // Muda o estado do objeto
        rivalidade.aceitar();

        // Salva a alteração no banco
        return rivalidadeRepository.save(rivalidade);
    }

    // Metodo 3: Recusar um convite
    public Rivalidade recusarConvite(RivalidadeId rivalidadeId, PerfilId idUsuarioQueRecusou) {
        Rivalidade rivalidade = rivalidadeRepository.findById(rivalidadeId)
                .orElseThrow(() -> new IllegalArgumentException("Rivalidade não encontrada."));

        // VERIFICAÇÃO DE SEGURANÇA
        if (!rivalidade.getPerfil2().equals(idUsuarioQueRecusou)) {
            throw new SecurityException("Usuário não autorizado a recusar este convite.");
        }

        rivalidade.recusar();
        return rivalidadeRepository.save(rivalidade);
    }

    public Rivalidade finalizarRivalidade(RivalidadeId rivalidadeId, PerfilId idUsuarioQueFinalizou) {
        Rivalidade rivalidade = rivalidadeRepository.findById(rivalidadeId)
                .orElseThrow(() -> new IllegalArgumentException("Rivalidade não encontrada."));

        // VERIFICAÇÃO DE SEGURANÇA: Garante que quem está finalizando é um dos participantes.
        boolean isParticipante = rivalidade.getPerfil1().equals(idUsuarioQueFinalizou) ||
                rivalidade.getPerfil2().equals(idUsuarioQueFinalizou);

        if (!isParticipante) {
            throw new SecurityException("Usuário não autorizado a finalizar esta rivalidade.");
        }

        // Delega a mudança de estado para a própria entidade
        rivalidade.finalizar();

        // Salva a alteração
        return rivalidadeRepository.save(rivalidade);
    }

}