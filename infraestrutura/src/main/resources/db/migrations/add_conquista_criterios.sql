-- Adicionar colunas de critérios para conquistas automáticas
ALTER TABLE conquista 
ADD COLUMN peso_minimo FLOAT NULL,
ADD COLUMN atributo_minimo INT NULL,
ADD COLUMN tipo_atributo VARCHAR(50) NULL,
ADD COLUMN repeticoes_minimas INT NULL,
ADD COLUMN series_minimas INT NULL;

