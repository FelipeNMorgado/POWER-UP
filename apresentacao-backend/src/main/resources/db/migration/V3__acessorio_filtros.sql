-- Ajustes de colunas para filtros da loja (MySQL)
-- Usa SQL dinâmico para ser idempotente em qualquer versão de MySQL

-- qualidade
SET @stmt := (
    SELECT IF(
        EXISTS(
            SELECT 1
            FROM information_schema.columns
            WHERE table_schema = DATABASE()
              AND table_name = 'acessorio'
              AND column_name = 'qualidade'
        ),
        'SELECT 1',
        'ALTER TABLE acessorio ADD COLUMN qualidade VARCHAR(50) NOT NULL DEFAULT ''Basica'''
    )
);
PREPARE s FROM @stmt;
EXECUTE s;
DEALLOCATE PREPARE s;

-- categoria
SET @stmt := (
    SELECT IF(
        EXISTS(
            SELECT 1
            FROM information_schema.columns
            WHERE table_schema = DATABASE()
              AND table_name = 'acessorio'
              AND column_name = 'categoria'
        ),
        'SELECT 1',
        'ALTER TABLE acessorio ADD COLUMN categoria VARCHAR(50) NOT NULL DEFAULT ''Acessorio'''
    )
);
PREPARE s FROM @stmt;
EXECUTE s;
DEALLOCATE PREPARE s;

-- subcategoria
SET @stmt := (
    SELECT IF(
        EXISTS(
            SELECT 1
            FROM information_schema.columns
            WHERE table_schema = DATABASE()
              AND table_name = 'acessorio'
              AND column_name = 'subcategoria'
        ),
        'SELECT 1',
        'ALTER TABLE acessorio ADD COLUMN subcategoria VARCHAR(50)'
    )
);
PREPARE s FROM @stmt;
EXECUTE s;
DEALLOCATE PREPARE s;

