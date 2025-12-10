package Up.Power.mocks;

import Up.Power.PlanoNutricional;
import Up.Power.planoNutricional.PlanoNId;
import Up.Power.planoNutricional.PlanoNutricionalRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlanoNutricionalMock implements PlanoNutricionalRepository {

    private final Map<Integer, PlanoNutricional> bancoMemoria;

    public PlanoNutricionalMock() {
        this.bancoMemoria = new HashMap<>();
    }

    @Override
    public void salvar(PlanoNutricional planoN) {
        if (planoN == null || planoN.getObjetivo() == null) {
            throw new IllegalArgumentException("Campos obrigatórios em branco");
        }
        bancoMemoria.put(planoN.getId().getId(), planoN);
        System.out.println("Plano nutricional salvo: " + planoN.getId().getId());
    }

    @Override
    public PlanoNutricional obter(PlanoNId planoId) {
        return bancoMemoria.get(planoId.getId());
    }

    @Override
    public List<PlanoNutricional> listarPorUsuario(String usuarioEmail) {
        return bancoMemoria.values().stream()
                .filter(plano -> plano.getUsuarioEmail() != null && 
                        plano.getUsuarioEmail().getCaracteres().equals(usuarioEmail))
                .collect(Collectors.toList());
    }

    @Override
    public void excluir(PlanoNId id) {
        bancoMemoria.remove(id.getId());
    }

    public void limpar() {
        bancoMemoria.clear();
    }

    public boolean contem(PlanoNId id) {
        return bancoMemoria.containsKey(id.getId());
    }

    public int tamanho() {
        return bancoMemoria.size();
    }
}

