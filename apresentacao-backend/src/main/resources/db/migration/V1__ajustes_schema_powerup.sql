-- Cria/ajusta tabelas conforme entidades atuais

-- USUARIO
CREATE TABLE IF NOT EXISTS usuario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    amizade_id INT NULL,
    nome VARCHAR(255),
    senha VARCHAR(255),
    data_nascimento DATE
);
ALTER TABLE usuario
  ADD COLUMN IF NOT EXISTS amizade_id INT NULL,
  ADD COLUMN IF NOT EXISTS data_nascimento DATE NULL;

-- PERFIL
CREATE TABLE IF NOT EXISTS perfil (
    id INT AUTO_INCREMENT PRIMARY KEY,
    usuario_email VARCHAR(255) NOT NULL,
    username VARCHAR(255) NOT NULL,
    estado BIT NOT NULL,
    criacao DATETIME NOT NULL,
    foto LONGTEXT,
    conquistas_selecionadas VARCHAR(255)
);
ALTER TABLE perfil
  ADD COLUMN IF NOT EXISTS conquistas_selecionadas VARCHAR(255) NULL;

-- AVATAR
CREATE TABLE IF NOT EXISTS avatar (
    id INT AUTO_INCREMENT PRIMARY KEY,
    perfil_id INT NOT NULL,
    nivel INT NOT NULL,
    experiencia INT NOT NULL,
    dinheiro INT NOT NULL,
    forca INT NOT NULL
);
ALTER TABLE avatar
  ADD COLUMN IF NOT EXISTS perfil_id INT NOT NULL,
  ADD COLUMN IF NOT EXISTS nivel INT NOT NULL,
  ADD COLUMN IF NOT EXISTS experiencia INT NOT NULL,
  ADD COLUMN IF NOT EXISTS dinheiro INT NOT NULL,
  ADD COLUMN IF NOT EXISTS forca INT NOT NULL;

-- AVATAR_ACESSORIOS (coleção do avatar)
CREATE TABLE IF NOT EXISTS avatar_acessorios (
    avatar_id INT NOT NULL,
    acessorio_id INT NOT NULL,
    PRIMARY KEY (avatar_id, acessorio_id)
);

-- CONQUISTA
CREATE TABLE IF NOT EXISTS conquista (
    id INT AUTO_INCREMENT PRIMARY KEY,
    exercicio_id INT NOT NULL,
    treino_id INT NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    nome VARCHAR(255) NOT NULL
);
ALTER TABLE conquista
  ADD COLUMN IF NOT EXISTS concluida TINYINT(1) NOT NULL DEFAULT 0,
  ADD COLUMN IF NOT EXISTS badge VARCHAR(255) NULL,
  ADD COLUMN IF NOT EXISTS peso_minimo FLOAT NULL,
  ADD COLUMN IF NOT EXISTS atributo_minimo INT NULL,
  ADD COLUMN IF NOT EXISTS tipo_atributo VARCHAR(50) NULL,
  ADD COLUMN IF NOT EXISTS repeticoes_minimas INT NULL,
  ADD COLUMN IF NOT EXISTS series_minimas INT NULL;

-- FREQUENCIA
CREATE TABLE IF NOT EXISTS frequencia (
    id INT AUTO_INCREMENT PRIMARY KEY,
    perfil_id INT NOT NULL,
    treino_id INT NOT NULL,
    data_presenca DATETIME NOT NULL,
    plano_treino_id INT NOT NULL,
    foto TEXT NULL
);
ALTER TABLE frequencia
  ADD COLUMN IF NOT EXISTS perfil_id INT NOT NULL,
  ADD COLUMN IF NOT EXISTS treino_id INT NOT NULL,
  ADD COLUMN IF NOT EXISTS data_presenca DATETIME NOT NULL,
  ADD COLUMN IF NOT EXISTS plano_treino_id INT NOT NULL,
  ADD COLUMN IF NOT EXISTS foto TEXT NULL;

-- FEEDBACK
CREATE TABLE IF NOT EXISTS feedback (
    id INT AUTO_INCREMENT PRIMARY KEY,
    frequencia_id INT NOT NULL,
    classificacao VARCHAR(255) NOT NULL,
    feedback_texto TEXT NULL,
    email_usuario VARCHAR(255) NULL,
    data DATETIME NULL
);
ALTER TABLE feedback
  ADD COLUMN IF NOT EXISTS frequencia_id INT NOT NULL,
  ADD COLUMN IF NOT EXISTS classificacao VARCHAR(255) NOT NULL,
  ADD COLUMN IF NOT EXISTS feedback_texto TEXT NULL,
  ADD COLUMN IF NOT EXISTS email_usuario VARCHAR(255) NULL,
  ADD COLUMN IF NOT EXISTS data DATETIME NULL;

-- RELACIONAMENTOS MANY-TO-MANY USADOS EM PerfilJpa
CREATE TABLE IF NOT EXISTS perfil_conquista (
    perfil_id INT NOT NULL,
    conquista_id INT NOT NULL,
    PRIMARY KEY (perfil_id, conquista_id)
);

CREATE TABLE IF NOT EXISTS perfil_amigos (
    perfil_id INT NOT NULL,
    amigo_perfil_id INT NOT NULL,
    PRIMARY KEY (perfil_id, amigo_perfil_id)
);

CREATE TABLE IF NOT EXISTS perfil_plano_treino (
    perfil_id INT NOT NULL,
    plano_treino_id INT NOT NULL,
    PRIMARY KEY (perfil_id, plano_treino_id)
);

CREATE TABLE IF NOT EXISTS perfil_meta (
    perfil_id INT NOT NULL,
    meta_id INT NOT NULL,
    PRIMARY KEY (perfil_id, meta_id)
);

