package Up.Power.meta;

import Up.Power.Meta;
import java.time.LocalDate;
import java.util.Date;

public class RewardServiceImpl implements RewardService {
    
    @Override
    public boolean canCollectRewards(Meta meta, LocalDate hoje) {
        // Regra: se a meta passou do prazo (fim < hoje), não pode coletar recompensas
        Date fim = meta.getFim();
        if (fim == null) {
            return false; // Se não tem data de fim, não pode coletar
        }
        
        LocalDate fimLocal = new java.sql.Date(fim.getTime()).toLocalDate();
        return !fimLocal.isBefore(hoje); // só pode coletar se ainda não passou do prazo
    }
}

