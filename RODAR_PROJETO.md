# Como Rodar o Projeto

## Backend (Spring Boot)

1. **Navegar para a pasta do backend:**
```powershell
cd C:\Users\guirtoon\Desktop\POWER-UP\POWER-UP-BACKEND\apresentacao-backend
```

2. **Rodar o projeto:**
```powershell
mvn spring-boot:run
```

O backend estará disponível em: `http://localhost:8080`

## Frontend (React + Vite)

1. **Abrir um novo terminal e navegar para a pasta do frontend:**
```powershell
cd C:\Users\guirtoon\Desktop\POWER-UP\POWER-UP-FRONTEND
```

2. **Rodar o projeto:**
```powershell
npm run dev
```

O frontend estará disponível em: `http://localhost:5173` ou `http://localhost:5174`

## MySQL (Docker)

Se o MySQL não estiver rodando:

```powershell
cd C:\Users\guirtoon\Desktop\POWER-UP
docker-compose up -d
```

