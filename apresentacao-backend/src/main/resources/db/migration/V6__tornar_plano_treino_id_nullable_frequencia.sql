-- Tornar planoTreinoId nullable na tabela frequencia
-- Isso permite que frequências sejam mantidas mesmo quando o plano de treino é deletado
-- A ida à academia não deve ser anulada só porque o plano foi deletado

-- Tornar a coluna nullable (idempotente)
SET @stmt := (
    SELECT IF(
        EXISTS(
            SELECT 1
            FROM information_schema.columns
            WHERE table_schema = DATABASE()
              AND table_name = 'frequencia'
              AND column_name = 'plano_treino_id'
              AND is_nullable = 'NO'
        ),
        'ALTER TABLE frequencia MODIFY COLUMN plano_treino_id INT NULL',
        'SELECT 1'
    )
);
PREPARE s FROM @stmt;
EXECUTE s;
DEALLOCATE PREPARE s;

