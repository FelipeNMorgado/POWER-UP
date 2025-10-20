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

        Rivalidade novoConvite = new Rivalidade(idPerfil1, idPerfil2, exercicioId);
        return rivalidadeRepository.save(novoConvite);
    }

    public Rivalidade aceitarConvite(RivalidadeId rivalidadeId, PerfilId idUsuarioQueAceitou) {
        Rivalidade rivalidade = rivalidadeRepository.findById(rivalidadeId)
                .orElseThrow(() -> new IllegalArgumentException("Rivalidade não encontrada."));

        if (!rivalidade.getPerfil2().equals(idUsuarioQueAceitou)) {
            throw new SecurityException("Usuário não autorizado a aceitar este convite.");
        }

        rivalidade.aceitar();

        return rivalidadeRepository.save(rivalidade);
    }

    public Rivalidade recusarConvite(RivalidadeId rivalidadeId, PerfilId idUsuarioQueRecusou) {
        Rivalidade rivalidade = rivalidadeRepository.findById(rivalidadeId)
                .orElseThrow(() -> new IllegalArgumentException("Rivalidade não encontrada."));

        if (!rivalidade.getPerfil2().equals(idUsuarioQueRecusou)) {
            throw new SecurityException("Usuário não autorizado a recusar este convite.");
        }

        rivalidade.recusar();
        return rivalidadeRepository.save(rivalidade);
    }

    public Rivalidade finalizarRivalidade(RivalidadeId rivalidadeId, PerfilId idUsuarioQueFinalizou) {
        Rivalidade rivalidade = rivalidadeRepository.findById(rivalidadeId)
                .orElseThrow(() -> new IllegalArgumentException("Rivalidade não encontrada."));

        boolean isParticipante = rivalidade.getPerfil1().equals(idUsuarioQueFinalizou) ||
                rivalidade.getPerfil2().equals(idUsuarioQueFinalizou);

        if (!isParticipante) {
            throw new SecurityException("Usuário não autorizado a finalizar esta rivalidade.");
        }

        rivalidade.finalizar();

        return rivalidadeRepository.save(rivalidade);
    }

}