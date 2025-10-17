package Up.Power.mocks;

import Up.Power.Equipe;
import Up.Power.equipe.EquipeId;
import Up.Power.equipe.EquipeRepository;
import Up.Power.perfil.PerfilId;

import java.util.*;

public class EquipeMock implements EquipeRepository {

    private final Map<Integer, Equipe> equipesPorId = new HashMap<>();

    /**
     * "Persiste" a equipe em memória usando seu id como chave.
     * Lança IllegalArgumentException quando a equipe ou seu id são nulos
     * para simular validação básica de repositório.
     */
    @Override
    public void salvar(Equipe equipe) {
        if (equipe == null || equipe.getId() == null) {
            throw new IllegalArgumentException("Equipe inválida");
        }
        equipesPorId.put(equipe.getId().getId(), equipe);
    }

    /**
     * Retorna equipes conforme os filtros recebidos:
     * - Se id != null: retorna a equipe específica (ou vazio)
     * - Caso contrário: retorna todas as equipes "persistidas"
     * Observação: o parâmetro 'perfil' não é utilizado neste mock por não haver
     *             armazenamento de vínculo equipe-perfil nos cenários atuais.
     */
    @Override
    public List<Equipe> listarEquipe(EquipeId id, PerfilId perfil) {
        if (id != null) {
            Equipe equipe = equipesPorId.get(id.getId());
            return equipe == null ? Collections.emptyList() : Collections.singletonList(equipe);
        }
        return new ArrayList<>(equipesPorId.values());
    }

}


