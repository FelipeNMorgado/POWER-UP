-- Script para alterar coluna foto na tabela frequencia de TEXT para LONGTEXT
-- Execute este script diretamente no banco de dados MySQL

USE powerup;

-- Alterar a coluna foto para LONGTEXT
ALTER TABLE frequencia MODIFY COLUMN foto LONGTEXT NULL;

-- Verificar se a alteração foi aplicada
SELECT COLUMN_NAME, DATA_TYPE, CHARACTER_MAXIMUM_LENGTH 
FROM information_schema.COLUMNS 
WHERE TABLE_SCHEMA = 'powerup' 
  AND TABLE_NAME = 'frequencia' 
  AND COLUMN_NAME = 'foto';

