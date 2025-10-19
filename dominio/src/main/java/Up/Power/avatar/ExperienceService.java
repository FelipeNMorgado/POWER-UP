package Up.Power.avatar;

import Up.Power.Avatar;

public interface ExperienceService {
    /**
     * Adiciona XP a um avatar e o sobe de nível se necessário.
     */
    void adicionarXp(Avatar avatar, int xpGanha);

    /**
     * Adiciona pontos de força a um avatar.
     */
    void adicionarForca(Avatar avatar, int bonusDeForca);
}