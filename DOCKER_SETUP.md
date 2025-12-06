# Configuração MySQL com Docker

Este projeto usa MySQL rodando em um container Docker para facilitar o desenvolvimento.

## 🔗 Links Rápidos

- **Aplicação:** [http://localhost:8080](http://localhost:8080)
- **MySQL (Docker):** localhost:3306

## Pré-requisitos

- Docker instalado e rodando
- Docker Compose instalado (geralmente vem com o Docker Desktop)

## Como usar

### 1. Iniciar o MySQL

```bash
# Na raiz do projeto
docker-compose up -d
```

Isso vai:
- Baixar a imagem do MySQL 8.0 (se não tiver)
- Criar e iniciar o container `powerup-mysql`
- Criar o banco de dados `powerup` automaticamente
- Mapear a porta 3306 do container para a porta 3306 do seu computador

### 2. Verificar se está rodando

```bash
docker-compose ps
```

Você deve ver o container `powerup-mysql` com status "Up".

### 3. Executar a aplicação

```bash
cd apresentacao-backend
mvn spring-boot:run
```

A aplicação vai conectar automaticamente ao MySQL no Docker.

### 4. Acessar a aplicação

Após a aplicação iniciar com sucesso, você verá uma mensagem similar a:
```
Started BackendAplicacao in X.XXX seconds
```

A aplicação estará disponível em:

**🔗 [http://localhost:8080](http://localhost:8080)**

#### Passos para acessar:

1. **Aguarde a aplicação iniciar completamente** (procure pela mensagem "Started BackendAplicacao")
2. **Abra seu navegador** (Chrome, Firefox, Edge, etc.)
3. **Acesse o link:** [http://localhost:8080](http://localhost:8080)
   - Ou digite manualmente: `http://localhost:8080` na barra de endereços

#### Verificar se a aplicação está rodando:

- **No terminal:** Você verá logs do Spring Boot indicando que o servidor está rodando
- **No navegador:** Se a aplicação tiver endpoints configurados, você verá a resposta ou a interface da aplicação
- **Teste de conexão:** Se não houver interface, mas a aplicação iniciou sem erros, significa que está conectada ao MySQL e pronta para receber requisições

### 5. Parar a aplicação

Para parar a aplicação Spring Boot:
- **No terminal onde está rodando:** Pressione `Ctrl + C`
- Aguarde alguns segundos para o Spring Boot encerrar graciosamente

### 6. Parar o MySQL

```bash
# Parar o container (mantém os dados)
docker-compose stop

# Parar e remover o container (mantém os dados no volume)
docker-compose down

# Parar, remover container E apagar todos os dados
docker-compose down -v
```

## Configurações do MySQL

- **Host:** localhost
- **Porta:** 3306
- **Banco de dados:** powerup
- **Usuário root:** root
- **Senha root:** root
- **Usuário adicional:** powerup_user
- **Senha adicional:** powerup_pass

## Ver logs do MySQL

```bash
docker-compose logs -f mysql
```

## Acessar o MySQL via linha de comando

```bash
# Entrar no container MySQL
docker exec -it powerup-mysql mysql -uroot -proot

# Ou acessar o banco powerup diretamente
docker exec -it powerup-mysql mysql -uroot -proot powerup
```

## Dados persistentes

Os dados são salvos em um volume Docker chamado `mysql_data`, então mesmo se você parar o container, os dados serão mantidos.

## Alterar senhas

Se quiser alterar as senhas, edite o arquivo `docker-compose.yml` e também o `application.properties`:

1. Edite `docker-compose.yml` (variáveis de ambiente)
2. Edite `apresentacao-backend/src/main/resources/application.properties` (senha do Spring)

## Troubleshooting

### Porta 3306 já em uso

Se você já tem MySQL instalado localmente na porta 3306, você pode:

1. **Parar o MySQL local** (recomendado para desenvolvimento)
2. **Ou alterar a porta no docker-compose.yml:**
   ```yaml
   ports:
     - "3307:3306"  # Mude 3306 para 3307 (ou outra porta)
   ```
   E atualize o `application.properties`:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3307/powerup?...
   ```

### Container não inicia

```bash
# Ver logs de erro
docker-compose logs mysql

# Recriar o container
docker-compose down -v
docker-compose up -d
```

