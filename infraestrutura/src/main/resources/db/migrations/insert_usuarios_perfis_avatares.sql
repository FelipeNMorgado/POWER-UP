-- Script para injetar 5 usuários com seus respectivos perfis e avatares
-- Executar na ordem: usuários -> perfis -> avatares

-- =====================================================
-- 1. INSERIR 5 USUÁRIOS
-- =====================================================
INSERT INTO usuario (email, nome, senha, data_nascimento) VALUES 
('joao.silva@example.com', 'João Silva', 'senha123', '1990-05-15'),
('maria.santos@example.com', 'Maria Santos', 'senha456', '1992-08-22'),
('carlos.oliveira@example.com', 'Carlos Oliveira', 'senha789', '1988-03-10'),
('ana.costa@example.com', 'Ana Costa', 'senhaABC', '1995-11-28'),
('pedro.ferreira@example.com', 'Pedro Ferreira', 'senhaXYZ', '1991-07-14');

-- =====================================================
-- 2. INSERIR 5 PERFIS (identificados pelo email do usuário)
-- =====================================================
INSERT INTO perfil (usuario_email, username, estado, criacao, foto) VALUES 
('joao.silva@example.com', 'joao_silva', true, NOW(), 'https://example.com/fotos/joao.jpg'),
('maria.santos@example.com', 'maria_santos', true, NOW(), 'https://example.com/fotos/maria.jpg'),
('carlos.oliveira@example.com', 'carlos_oliveira', true, NOW(), 'https://example.com/fotos/carlos.jpg'),
('ana.costa@example.com', 'ana_costa', true, NOW(), 'https://example.com/fotos/ana.jpg'),
('pedro.ferreira@example.com', 'pedro_ferreira', true, NOW(), 'https://example.com/fotos/pedro.jpg');

-- =====================================================
-- 3. INSERIR 5 AVATARES (identificados pelo id do perfil)
-- =====================================================
-- Obs: Os IDs dos perfis serão 1-5 se a sequência for respeitada
INSERT INTO avatar (perfil_id, nivel, experiencia, dinheiro, forca) VALUES 
(1, 1, 0, 1000, 10),
(2, 1, 0, 1000, 10),
(3, 1, 0, 1000, 10),
(4, 1, 0, 1000, 10),
(5, 1, 0, 1000, 10);

-- =====================================================
-- VERIFICAÇÃO: Consultas para validar a inserção
-- =====================================================
-- Usuários inseridos:
SELECT 'USUÁRIOS INSERIDOS' as info;
SELECT id, email, nome, data_nascimento FROM usuario 
WHERE email LIKE '%@example.com%' 
ORDER BY id DESC LIMIT 5;

-- Perfis inseridos:
SELECT 'PERFIS INSERIDOS' as info;
SELECT id, usuario_email, username, criacao FROM perfil 
WHERE usuario_email LIKE '%@example.com%' 
ORDER BY id DESC LIMIT 5;

-- Avatares inseridos:
SELECT 'AVATARES INSERIDOS' as info;
SELECT id, perfil_id, nivel, experiencia, dinheiro, forca FROM avatar 
WHERE perfil_id IN (1, 2, 3, 4, 5) 
ORDER BY id DESC LIMIT 5;

-- Relacionamento completo (Usuário -> Perfil -> Avatar):
SELECT 'RELACIONAMENTO COMPLETO' as info;
SELECT 
    u.id as usuario_id,
    u.email,
    u.nome,
    p.id as perfil_id,
    p.username,
    a.id as avatar_id,
    a.nivel,
    a.experiencia,
    a.dinheiro
FROM usuario u
LEFT JOIN perfil p ON u.email = p.usuario_email
LEFT JOIN avatar a ON p.id = a.perfil_id
WHERE u.email LIKE '%@example.com%'
ORDER BY u.id;
