package Up.Power.mocks;

import Up.Power.Meta;
import Up.Power.meta.RewardService;

import java.time.LocalDate;
import java.util.Date;

public class RewardServiceMock implements RewardService {
    @Override
    public boolean canCollectRewards(Meta meta, LocalDate hoje) {
        // Regra simples: se a meta passou do prazo (fim < hoje) e não foi concluída,
        // NÃO pode coletar. Como não temos flag "concluída", assumimos não concluída.
        Date fim = meta.getFim();
        LocalDate fimLocal = new java.sql.Date(fim.getTime()).toLocalDate();
        return !fimLocal.isBefore(hoje); // só pode coletar se ainda não passou do prazo
    }
}
