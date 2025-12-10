package Up.Power.avatar;

import Up.Power.Avatar;

public class ExperienceServiceImpl implements ExperienceService {

    private static final int XP_PARA_PROXIMO_NIVEL = 100;

    @Override
    public void adicionarXp(Avatar avatar, int xpGanha) {
        int xpAtual = avatar.getExperiencia();
        int novoXpTotal = xpAtual + xpGanha;

        // Lógica para subir de nível (pode subir múltiplos níveis se XP for suficiente)
        int nivelAtual = avatar.getNivel();
        int xpRestante = novoXpTotal;
        
        while (xpRestante >= XP_PARA_PROXIMO_NIVEL) {
            nivelAtual++;
            xpRestante -= XP_PARA_PROXIMO_NIVEL;
        }
        
        avatar.setNivel(nivelAtual);
        avatar.setExperiencia(xpRestante);
    }

    @Override
    public void adicionarForca(Avatar avatar, int bonusDeForca) {
        int forcaAtual = avatar.getForca();
        avatar.setForca(forcaAtual + bonusDeForca);
    }
}

