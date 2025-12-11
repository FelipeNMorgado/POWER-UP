-- Script para corrigir o histórico do Flyway após falha na migration V5
-- Execute este script no banco de dados MySQL antes de rodar a aplicação novamente

-- Opção 1: Remover o registro da migration falha (recomendado)
DELETE FROM flyway_schema_history WHERE version = '5' AND success = 0;

-- Opção 2: Se a coluna já existe, marcar como sucesso (descomente se necessário)
-- UPDATE flyway_schema_history 
-- SET success = 1, installed_on = NOW()
-- WHERE version = '5';

-- Verificar se a coluna existe e adicionar se não existir
SET @stmt := (
    SELECT IF(
        EXISTS(
            SELECT 1
            FROM information_schema.columns
            WHERE table_schema = DATABASE()
              AND table_name = 'meta'
              AND column_name = 'exigencia_minima'
        ),
        'SELECT "Coluna já existe" AS resultado',
        'ALTER TABLE meta ADD COLUMN exigencia_minima DECIMAL(10,2) NULL'
    )
);
PREPARE s FROM @stmt;
EXECUTE s;
DEALLOCATE PREPARE s;

