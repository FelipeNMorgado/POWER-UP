# Guia rapido: seed do banco powerup

## Opcao recomendada: limpar o container antes
Copiar e colar no PowerShell (na raiz do projeto, onde esta o docker-compose):
```bash
docker-compose down -v
docker-compose up -d
```
Isso remove volumes, recria o MySQL limpo e sobe novamente.

## Preparar ambiente Python (venv)
```bash
cd utils-scripts
python -m venv .venv
.\.venv\Scripts\activate   # Windows PowerShell
pip install -r requirements.txt
```

## (Importante) Gerar tabelas antes de rodar o seed
No backend (raiz do projeto), para criar o schema/tabelas via migrations:
```bash
cd backend
mvn clean install -DskipTests
mvn spring-boot:run -pl apresentacao-backend
```
Deixe subir e criar as tabelas; depois pode encerrar. Garanta que o MySQL esta no host/porta corretos (127.0.0.1:3307) e o schema `powerup` existe/esta como default.

## Rodar o seed
```bash
cd backend/utils-scripts
.\.venv\Scripts\activate   # se ainda nao estiver ativo
python seed_powerup.py
```
O script:
- Desliga FKs e trunca tabelas em ordem segura.
- Insere ~30 usuarios/perfis/avatares com XP variado.
- Cria amizades, equipes e membros.
- Gera feedbacks, conquistas (e vinculos) e frequencias.
- Popular loja: insere acessórios (com qualidade/categoria/subcategoria) e limpa `acessorio`/`avatar_acessorios` antes de inserir.

## Ajustes opcionais
- Edite `CONFIG` no topo de `seed_powerup.py` para host/porta/credenciais.
- Altere quantidades: `seed_usuarios_perfis_avatares(n=...)`, `seed_feedback(count=...)`, etc.

## Verificacao rapida (DBeaver ou MySQL)
```sql
SELECT COUNT(*) FROM usuario;
SELECT COUNT(*) FROM perfil;
SELECT COUNT(*) FROM avatar;
SELECT COUNT(*) FROM equipe;
SELECT COUNT(*) FROM equipe_membros;
SELECT COUNT(*) FROM feedback;
SELECT COUNT(*) FROM conquista;
SELECT COUNT(*) FROM perfil_conquista;
SELECT COUNT(*) FROM frequencia;
```

## Observacoes
- Assume schema criado pelas migrations.
- XP = (nivel - 1) * 100 + experiencia.
- Reproduzível: usa `random.seed(42)`.

