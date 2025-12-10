package Up.Power.planoNutricional;

import Up.Power.PlanoNutricional;
import Up.Power.Email;

public class PlanoNutricionalService {

    private final PlanoNutricionalRepository repository;

    public PlanoNutricionalService(PlanoNutricionalRepository repository) {
        this.repository = repository;
    }

    public void criarPlano(PlanoNId id, Objetivo objetivo, Email usuarioEmail) {
        if (id == null || objetivo == null || usuarioEmail == null) {
            throw new IllegalArgumentException("Campos obrigatórios em branco");
        }

        PlanoNutricional plano = new PlanoNutricional(id, objetivo, usuarioEmail);
        repository.salvar(plano);
    }

    public void modificarPlano(PlanoNId id, Objetivo novoObjetivo, Integer novasCaloriasObjetivo) {
        PlanoNutricional planoExistente = repository.obter(id);

        if (planoExistente == null) {
            throw new IllegalArgumentException("Plano não encontrado");
        }

        if (novoObjetivo == null) {
            throw new IllegalArgumentException("Campos obrigatórios em branco");
        }

        if (novasCaloriasObjetivo != null && novasCaloriasObjetivo <= 0) {
            throw new IllegalArgumentException("Valor inválido para atributo");
        }

        int calorias = (novasCaloriasObjetivo != null)
                ? novasCaloriasObjetivo
                : calcularCaloriasObjetivo(planoExistente.getCaloriasTotais(), novoObjetivo);

        // Manter o email do plano existente ao modificar
        PlanoNutricional planoAtualizado = new PlanoNutricional(id, novoObjetivo, planoExistente.getUsuarioEmail());
        planoAtualizado.adicionarRefeicaoList(planoExistente.getRefeicoes());
        planoAtualizado.definirCaloriasObjetivo(calorias);

        repository.salvar(planoAtualizado);
    }

    public int calcularCaloriasObjetivo(int caloriasTotais, Objetivo objetivo) {
        if (objetivo == Objetivo.Cutting) {
            return (int) (caloriasTotais * 0.8);
        } else if (objetivo == Objetivo.Bulking) {
            return (int) (caloriasTotais * 1.15);
        }
        return caloriasTotais;
    }

    public PlanoNutricional obterPlano(PlanoNId id) {
        PlanoNutricional plano = repository.obter(id);
        if (plano == null) {
            throw new IllegalArgumentException("Plano não encontrado");
        }
        return plano;
    }
}