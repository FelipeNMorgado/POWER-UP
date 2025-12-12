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

-- AVATAR
CREATE TABLE IF NOT EXISTS avatar (
    id INT AUTO_INCREMENT PRIMARY KEY,
    perfil_id INT NOT NULL,
    nivel INT NOT NULL,
    experiencia INT NOT NULL,
    dinheiro INT NOT NULL,
    forca INT NOT NULL
);

-- AVATAR_ACESSORIOS (coleção do avatar)
CREATE TABLE IF NOT EXISTS avatar_acessorios (
    avatar_id INT NOT NULL,
    acessorio_id INT NOT NULL,
    PRIMARY KEY (avatar_id, acessorio_id)
);

-- ACESSORIO
CREATE TABLE IF NOT EXISTS acessorio (
    id INT AUTO_INCREMENT PRIMARY KEY,
    icone LONGTEXT,
    preco INT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    imagem LONGTEXT,
    qualidade VARCHAR(50) NOT NULL DEFAULT 'Basica',
    categoria VARCHAR(50) NOT NULL DEFAULT 'Acessorio',
    subcategoria VARCHAR(50)
);

-- CONQUISTA
CREATE TABLE IF NOT EXISTS conquista (
    id INT AUTO_INCREMENT PRIMARY KEY,
    exercicio_id INT NOT NULL,
    treino_id INT NOT NULL,
    descricao VARCHAR(255) NOT NULL,
    nome VARCHAR(255) NOT NULL
);

-- META
CREATE TABLE IF NOT EXISTS meta (
    id INT AUTO_INCREMENT PRIMARY KEY,
    perfil_id INT NOT NULL,
    nome VARCHAR(255) NOT NULL,
    descricao TEXT,
    exigencia_minima DECIMAL(10,2) NULL
);

-- FREQUENCIA
CREATE TABLE IF NOT EXISTS frequencia (
    id INT AUTO_INCREMENT PRIMARY KEY,
    perfil_id INT NOT NULL,
    treino_id INT NOT NULL,
    data_presenca DATETIME NOT NULL,
    plano_treino_id INT NOT NULL,
    foto TEXT NULL
);

-- FEEDBACK
CREATE TABLE IF NOT EXISTS feedback (
    id INT AUTO_INCREMENT PRIMARY KEY,
    frequencia_id INT NOT NULL,
    classificacao VARCHAR(255) NOT NULL,
    feedback_texto TEXT NULL,
    email_usuario VARCHAR(255) NULL,
    data DATETIME NULL
);

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

