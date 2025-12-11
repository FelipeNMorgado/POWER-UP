package Up.Power.aplicacao.loja;

import java.util.List;

public record ItemLojaResumo(
        Integer id,
        String icone,
        String imagem,
        String nome,
        Integer preco,
        String qualidade,
        String categoria,
        String subcategoria,
        List<AcessorioResumo> acessorios
) {
    public record AcessorioResumo(
            Integer id,
            String icone,
            String imagem,
            String nome,
            Integer preco,
            String qualidade,
            String categoria,
            String subcategoria
    ) {}
    
    // Construtor simplificado para quando não há acessórios aninhados
    public ItemLojaResumo(Integer id, String icone, String imagem, String nome, Integer preco,
                          String qualidade, String categoria, String subcategoria) {
        this(id, icone, imagem, nome, preco, qualidade, categoria, subcategoria, List.of());
    }
}

