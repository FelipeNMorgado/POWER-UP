-- Script de migração para alterar a coluna 'foto' da tabela 'perfil' de VARCHAR para LONGTEXT
-- Isso permite armazenar imagens em base64 que podem ser muito grandes
-- LONGTEXT suporta até 4GB de dados

ALTER TABLE perfil MODIFY COLUMN foto LONGTEXT;

