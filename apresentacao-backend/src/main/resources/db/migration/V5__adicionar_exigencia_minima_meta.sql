-- Adicionar coluna exigencia_minima na tabela meta (idempotente)
SET @stmt := (
    SELECT IF(
        EXISTS(
            SELECT 1
            FROM information_schema.columns
            WHERE table_schema = DATABASE()
              AND table_name = 'meta'
              AND column_name = 'exigencia_minima'
        ),
        'SELECT 1',
        'ALTER TABLE meta ADD COLUMN exigencia_minima DECIMAL(10,2) NULL'
    )
);
PREPARE s FROM @stmt;
EXECUTE s;
DEALLOCATE PREPARE s;

