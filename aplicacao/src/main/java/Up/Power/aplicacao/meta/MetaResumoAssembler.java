package Up.Power.aplicacao.meta;

import Up.Power.Meta;

public final class MetaResumoAssembler {

    private MetaResumoAssembler() {
    }

    public static MetaResumo toResumo(Meta meta, boolean concluida) {
        
        // Estruturando as variáveis locais para evitar problemas de inferência de tipo
        Integer exercicioId = meta.getExercicioId() != null ? meta.getExercicioId().getId() : null;
        Integer treinoId = meta.getTreinoId() != null ? meta.getTreinoId().getId() : null;
        
        return new MetaResumo(
                meta.getId() != null ? meta.getId().getId() : null,
                exercicioId,
                treinoId,
                meta.getNome(),
                // Usando getDataFim() ou getFim() (ambos existem na sua classe Meta)
                meta.getDataFim(), 
                // Usando getInicio() (confirmado na sua classe Meta)
                meta.getInicio(),
                meta.getExigenciaMinima(),
                concluida
        );
    }
}