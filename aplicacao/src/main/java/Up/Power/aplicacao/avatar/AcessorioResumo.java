package Up.Power.aplicacao.avatar;

public record AcessorioResumo(
        Integer id,
        String icone,
        String imagem,
        String nome,
        Integer preco,
        String qualidade,
        String categoria,
        String subcategoria,
        boolean equipado
) {}

