-- Alterar coluna foto na tabela frequencia de TEXT para LONGTEXT
-- Isso permite armazenar imagens em base64 que podem ser muito grandes

-- Verificar e alterar a coluna (idempotente)
SET @column_type := (
    SELECT DATA_TYPE
    FROM information_schema.columns
    WHERE table_schema = DATABASE()
      AND table_name = 'frequencia'
      AND column_name = 'foto'
    LIMIT 1
);

SET @stmt := IF(
    @column_type IS NOT NULL AND @column_type != 'longtext',
    'ALTER TABLE frequencia MODIFY COLUMN foto LONGTEXT NULL',
    'SELECT 1 AS "Coluna já é LONGTEXT ou não existe"'
);

PREPARE s FROM @stmt;
EXECUTE s;
DEALLOCATE PREPARE s;

