# 🏋️‍♂️ **PowerUp — Gerenciador de Treino Fitness Gamificado**

## 📖 **Descrição Geral**

O **PowerUp** é um **ecossistema fitness gamificado** criado para **motivar e engajar pessoas na prática regular de exercícios físicos**, principalmente em ambientes de academia.  
Através da **gamificação** — uso de elementos de jogos aplicados ao contexto de treino —, o PowerUp transforma o ato de treinar em uma experiência **divertida, competitiva e recompensadora**.

O foco principal do sistema é **aumentar a adesão à rotina de exercícios**, incentivando tanto praticantes iniciantes quanto experientes a manterem a consistência e a evolução no condicionamento físico.

Cada usuário é representado por um **Avatar** que **evolui conforme o progresso em treinos, metas e conquistas**, refletindo visualmente o desempenho e a dedicação do praticante.

Link para o repositório do frontend: https://github.com/Breno-Lira/PowerUP-front.git

---

## 🎯 **Objetivo**

O problema que o PowerUp resolve é a **dificuldade que as pessoas têm em transformar o exercício físico em um hábito**.  
A aplicação busca:

- Incentivar a **frequência** nos treinos;  
- Tornar o processo mais **motivador e divertido**;  
- Criar uma **comunidade ativa e saudável**;  
- Estimular o **autocuidado e a disciplina** por meio de recompensas, metas e progressão visível.

Em resumo: o PowerUp é mais do que um gerenciador de treinos — é um **jogo da vida real** onde o prêmio é a **melhoria do corpo e da mente**.

---

## 🧠 **Domínio**

**Domínio principal:** *Gerenciador de treino fitness gamificado*  

O PowerUp organiza suas funcionalidades em **contextos de domínio** (bounded contexts), que definem claramente as regras e entidades principais da aplicação.

---

## Artefatos da Primeira Entrega

Todos os artefatos para a primeira entrega se localizam nesse Drive: <a href='https://drive.google.com/drive/folders/1x6q2LasNOxI4EHkG0MiLaJmt7UTWb4kx?usp=sharing'>DRIVE<a>

- descrição do domínio;
- mapa da história do usuário;
- protótipos de baixa fidelidade;
- apresentação;

Os artefatos abaixo estão localizados no repositório do projeto:

- especificações de teste;
- automação dos testes;
- código necessário para que os testes sejam bem sucedidos;

# Padrões de Projeto Adotados

| Padrão de Projeto | Descrição | Classes Envolvidas |
| :--- | :--- | :--- |
| **Proxy** | Um padrão estrutural que fornece um substituto ou placeholder para outro objeto controlar o acesso a ele. No projeto, ele é usado para adicionar funcionalidades transversais (logging e medição de tempo) às operações do repositório de feedback (listarPorUsuario, obter, criar, excluir e modificar) sem modificar a implementação real. | **FeedbackRepositorioAplicacao:** Define o contrato comum para FeedbackRepositorioProxy (Proxy) e FeedbackRepositorioReal (RealSubject)</br>`aplicacao/src/main/java/Up/Power/aplicacao/feedback/FeedbackRepositorioAplicacao.java` </br></br>**FeedbackRepositorioReal:** Implementação concreta que executa as operações reais `aplicacao/src/main/java/Up/Power/aplicacao/feedback/FeedbackRepositorioReal.java`</br></br>**FeedbackRepositorioProxy:** Proxy que intercepta chamadas e adiciona funcionalidades`aplicacao/src/main/java/Up/Power/aplicacao/feedback/FeedbackRepositorioProxy.java`</br></br>**FeedbackServicoAplicacao:** Usa a interface FeedbackRepositorioAplicacao sem saber se está usando Proxy ou RealSubject `aplicacao/src/main/java/Up/Power/aplicacao/feedback/FeedbackServicoAplicacao.java`|
| **Template Method** | Um padrão comportamental que define o esqueleto de um algoritmo na classe base, permitindo que subclasses implementem os passos específicos sem alterar a estrutura geral. No contexto de Rivalidade, ele garante que todas as operações (enviar convite, aceitar, recusar, finalizar e cancelar) seguem o mesmo fluxo: validação → execução da operação → conversão para resumo. A classe template define métodos específicos para cada tipo de operação (`executarEnviarConvite`, `executarAceitar`, etc.), cada um seguindo o mesmo padrão de fluxo. As subclasses implementam apenas os métodos de execução específicos de cada operação, recebendo parâmetros diretos. | **OperacaoRivalidadeTemplate:** Classe abstrata que define os templates com métodos específicos para cada operação (executarEnviarConvite, executarAceitar, executarRecusar, executarFinalizar, executarCancelar), cada um contendo o fluxo fixo e ganchos de validação</br>`aplicacao/src/main/java/Up/Power/aplicacao/rivalidade/template/OperacaoRivalidadeTemplate.java`</br></br>**EnviarConviteOperacao:** Implementação concreta que sobrescreve `executarOperacaoEnviarConvite(int perfil1Id, int perfil2Id, int exercicioId)` para enviar um convite de rivalidade `aplicacao/src/main/java/Up/Power/aplicacao/rivalidade/operacoes/EnviarConviteOperacao.java`</br></br>**AceitarRivalidadeOperacao:** Implementação concreta que sobrescreve `executarOperacaoAceitar(int rivalidadeId, int usuarioId)` para aceitar um convite `aplicacao/src/main/java/Up/Power/aplicacao/rivalidade/operacoes/AceitarRivalidadeOperacao.java`</br></br>**RecusarRivalidadeOperacao:** Implementação concreta que sobrescreve `executarOperacaoRecusar(int rivalidadeId, int usuarioId)` para recusar um convite `aplicacao/src/main/java/Up/Power/aplicacao/rivalidade/operacoes/RecusarRivalidadeOperacao.java`</br></br>**FinalizarRivalidadeOperacao:** Implementação concreta que sobrescreve `executarOperacaoFinalizar(int rivalidadeId, int usuarioId)` para finalizar uma rivalidade `aplicacao/src/main/java/Up/Power/aplicacao/rivalidade/operacoes/FinalizarRivalidadeOperacao.java`</br></br>**CancelarRivalidadeOperacao:** Implementação concreta que sobrescreve `executarOperacaoCancelar(int rivalidadeId, int usuarioId)` para cancelar uma rivalidade `aplicacao/src/main/java/Up/Power/aplicacao/rivalidade/operacoes/CancelarRivalidadeOperacao.java`</br></br>**RivalidadeServicoAplicacao:** Serviço que orquestra as operações, recebendo parâmetros diretos e delegando para as operações específicas `aplicacao/src/main/java/Up/Power/aplicacao/rivalidade/RivalidadeServicoAplicacao.java`|
