package Up.Power.meta;

import Up.Power.Meta;
import java.time.LocalDate;

public interface RewardService {
    boolean canCollectRewards(Meta meta, LocalDate hoje);
}
