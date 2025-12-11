-- Ajustes para tabela feedback conforme mapeamento JPA (FeedbackJpa)
-- Garante colunas basicas exigidas pelo seed e pelo backend

-- Remove colunas se existirem (compatível com MySQL 8 sem DROP COLUMN IF EXISTS)
SET @c := (SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'feedback' AND column_name = 'frequencia_id');
SET @sql := IF(@c > 0, 'ALTER TABLE feedback DROP COLUMN frequencia_id', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @c := (SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'feedback' AND column_name = 'classificacao');
SET @sql := IF(@c > 0, 'ALTER TABLE feedback DROP COLUMN classificacao', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @c := (SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'feedback' AND column_name = 'feedback_texto');
SET @sql := IF(@c > 0, 'ALTER TABLE feedback DROP COLUMN feedback_texto', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @c := (SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'feedback' AND column_name = 'email_usuario');
SET @sql := IF(@c > 0, 'ALTER TABLE feedback DROP COLUMN email_usuario', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

SET @c := (SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'feedback' AND column_name = 'data');
SET @sql := IF(@c > 0, 'ALTER TABLE feedback DROP COLUMN data', 'SELECT 1');
PREPARE stmt FROM @sql; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- Cria colunas conforme mapeamento JPA
ALTER TABLE feedback ADD COLUMN frequencia_id INT NOT NULL;
ALTER TABLE feedback ADD COLUMN classificacao VARCHAR(255) NOT NULL;
ALTER TABLE feedback ADD COLUMN feedback_texto TEXT NULL;
ALTER TABLE feedback ADD COLUMN email_usuario VARCHAR(255) NULL;
ALTER TABLE feedback ADD COLUMN data DATETIME NULL;

