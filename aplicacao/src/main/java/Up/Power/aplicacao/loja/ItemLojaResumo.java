package Up.Power.aplicacao.loja;

import java.util.List;

public record ItemLojaResumo(
        Integer id,
        String icone,
        String imagem,
        String nome,
        Integer preco,
        List<AcessorioResumo> acessorios
) {
    public record AcessorioResumo(
            Integer id,
            String icone,
            String imagem,
            String nome,
            Integer preco
    ) {}
    
    // Construtor simplificado para quando não há acessórios aninhados
    public ItemLojaResumo(Integer id, String icone, String imagem, String nome, Integer preco) {
        this(id, icone, imagem, nome, preco, List.of());
    }
}

