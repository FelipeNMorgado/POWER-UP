-- Garante tabela 'exercicio' necessária para FK (primeira execução)
CREATE TABLE IF NOT EXISTS exercicio (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(255) NULL
);

-- Cria tabela de progresso de treino por perfil e exercício
CREATE TABLE IF NOT EXISTS treino_progresso (
    id INT AUTO_INCREMENT PRIMARY KEY,
    perfil_id INT NOT NULL,
    exercicio_id INT NOT NULL,
    data_registro DATE NOT NULL,
    peso_kg DECIMAL(6,2) NULL,
    repeticoes INT NULL,
    series INT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_treino_progresso_perfil FOREIGN KEY (perfil_id) REFERENCES perfil(id),
    CONSTRAINT fk_treino_progresso_exercicio FOREIGN KEY (exercicio_id) REFERENCES exercicio(id)
);

