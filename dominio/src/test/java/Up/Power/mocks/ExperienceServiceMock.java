package Up.Power.mocks;

import Up.Power.Avatar;
import Up.Power.avatar.ExperienceService;

public class ExperienceServiceMock implements ExperienceService {

    private static final int XP_PARA_PROXIMO_NIVEL = 100;

    @Override
    public void adicionarXp(Avatar avatar, int xpGanha) {
        int xpAtual = avatar.getExperiencia();
        int novoXpTotal = xpAtual + xpGanha;

        // Lógica para subir de nível
        if (novoXpTotal >= XP_PARA_PROXIMO_NIVEL) {
            int nivelAtual = avatar.getNivel();
            avatar.setNivel(nivelAtual + 1);
            // Zera o XP ou subtrai o custo, dependendo da regra de negócio
            avatar.setExperiencia(novoXpTotal - XP_PARA_PROXIMO_NIVEL);
        } else {
            avatar.setExperiencia(novoXpTotal);
        }
    }

    @Override
    public void adicionarForca(Avatar avatar, int bonusDeForca) {
        int forcaAtual = avatar.getForca();
        avatar.setForca(forcaAtual + bonusDeForca);
    }
}