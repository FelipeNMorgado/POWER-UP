package Up.Power.rivalidade;


import Up.Power.Rivalidade;
import Up.Power.exercicio.ExercicioId;
import Up.Power.perfil.PerfilId;
import Up.Power.perfil.PerfilRepository;

public class RivalidadeService {

    private final RivalidadeRepository rivalidadeRepository;

    public RivalidadeService(RivalidadeRepository rivalidadeRepository, PerfilRepository perfilRepository) {
        this.rivalidadeRepository = rivalidadeRepository;
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

        // Verificar se o usuário que está aceitando já está em uma rivalidade ativa
        if (rivalidadeRepository.existsActiveRivalryForPerfil(idUsuarioQueAceitou)) {
            // Recusar automaticamente o convite
            rivalidade.recusar();
            rivalidadeRepository.save(rivalidade);
            throw new IllegalStateException("Você já está participando de uma rivalidade. O convite foi cancelado.");
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

    public Rivalidade cancelarConvite(RivalidadeId rivalidadeId, PerfilId idUsuarioQueCancelou) {
        Rivalidade rivalidade = rivalidadeRepository.findById(rivalidadeId)
                .orElseThrow(() -> new IllegalArgumentException("Rivalidade não encontrada."));

        // Apenas o perfil1 (quem enviou o convite) pode cancelar
        if (!rivalidade.getPerfil1().equals(idUsuarioQueCancelou)) {
            throw new SecurityException("Usuário não autorizado a cancelar este convite. Apenas quem enviou o convite pode cancelá-lo.");
        }

        rivalidade.cancelar();
        return rivalidadeRepository.save(rivalidade);
    }

}