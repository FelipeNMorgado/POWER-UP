package Up.Power.planoNutricional;

import Up.Power.PlanoNutricional;

/**
 * Classe responsável pelas regras de negócio do plano nutricional.
 */
public class PlanoNutricionalService {

    private final PlanoNutricionalRepository repository;

    public PlanoNutricionalService(PlanoNutricionalRepository repository) {
        this.repository = repository;
    }

    /**
     * Cria um novo plano nutricional e salva no repositório.
     */
    public void criarPlano(PlanoNId id, Objetivo objetivo) {
        if (id == null || objetivo == null) {
            throw new IllegalArgumentException("Campos obrigatórios em branco");
        }

        PlanoNutricional plano = new PlanoNutricional(id, objetivo);
        repository.salvar(plano);
    }

    /**
     * Modifica o objetivo ou calorias de um plano existente.
     */
    public void modificarPlano(PlanoNId id, Objetivo novoObjetivo, Integer novasCaloriasObjetivo) {
        PlanoNutricional planoExistente = repository.obter(id);

        if (planoExistente == null) {
            throw new IllegalArgumentException("Plano não encontrado");
        }

        if (novoObjetivo == null) {
            throw new IllegalArgumentException("Campos obrigatórios em branco");
        }

        // 🚨 Nova validação para impedir valores inválidos
        if (novasCaloriasObjetivo != null && novasCaloriasObjetivo <= 0) {
            throw new IllegalArgumentException("Valor inválido para atributo");
        }

        int calorias = (novasCaloriasObjetivo != null)
                ? novasCaloriasObjetivo
                : calcularCaloriasObjetivo(planoExistente.getCaloriasTotais(), novoObjetivo);

        PlanoNutricional planoAtualizado = new PlanoNutricional(id, novoObjetivo);
        planoAtualizado.adicionarRefeicaoList(planoExistente.getRefeicoes());
        planoAtualizado.definirCaloriasObjetivo(calorias);

        repository.salvar(planoAtualizado);
    }


    /**
     * Regra de negócio: calcular calorias conforme objetivo.
     * - Cutting: -20% das calorias
     * - Bulking: +15% das calorias
     */
    public int calcularCaloriasObjetivo(int caloriasTotais, Objetivo objetivo) {
        if (objetivo == Objetivo.Cutting) {
            return (int) (caloriasTotais * 0.8);
        } else if (objetivo == Objetivo.Bulking) {
            return (int) (caloriasTotais * 1.15);
        }
        return caloriasTotais;
    }

    /**
     * Verifica se os dados do plano são válidos.
     */
    public boolean validarCamposObrigatorios(PlanoNutricional plano) {
        return plano != null && plano.getObjetivo() != null && plano.getId() != null;
    }
}
