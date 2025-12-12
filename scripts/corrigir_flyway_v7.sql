-- Script para corrigir o histórico do Flyway após modificação da migration V7
-- Execute este script no banco de dados MySQL antes de rodar a aplicação novamente

-- Opção 1: Remover o registro da migration V7 para que seja executada novamente (recomendado)
DELETE FROM flyway_schema_history WHERE version = '7';

-- Opção 2: Se preferir atualizar o checksum (descomente se necessário)
-- UPDATE flyway_schema_history 
-- SET checksum = 626337807
-- WHERE version = '7';

