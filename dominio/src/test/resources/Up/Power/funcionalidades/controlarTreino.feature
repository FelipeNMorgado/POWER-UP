Feature: Controle personalizado do treino

  Scenario: Criar rotina de treino
    Given que o usuário esteja logado
    When criar uma rotina e adicionar exercícios do banco pré-existentes
    Then a rotina é salva com séries, repetições, carga e descanso definidos por ele

  Scenario: Validar plano mínimo
    Given criação de uma rotina
    When o plano não tiver ao menos um exercício definido
    Then o sistema bloqueia o salvamento e informa o requisito mínimo

  Scenario: Editar rotina
    Given uma rotina já criada
    When alterar séries, repetições, carga, descanso ou exercício
    Then o sistema atualiza a rotina com os novos parâmetros

  Scenario: Visualizar rotinas de treino
    Given que o usuário possua rotinas cadastradas
    When acessar a aba de treinos
    Then o sistema lista as rotinas e exibe os detalhes de cada exercício (carga, repetição e descanso)

  Scenario: Adicionar dia ao plano de treino
    Given uma rotina já criada
    When adicionar um dia da semana ao plano
    Then o dia é adicionado ao plano de treino

  Scenario: Alterar estado do plano
    Given uma rotina já criada
    When alterar o estado do plano para histórico
    Then o estado do plano é atualizado

  Scenario: Remover treino do plano
    Given uma rotina já criada
    When remover um exercício do plano
    Then o exercício é removido do plano de treino

  Scenario: Adicionar treino a plano existente
    Given uma rotina já criada
    When adicionar um novo exercício ao plano
    Then o treino é adicionado ao plano

  Scenario: Excluir plano de treino
    Given uma rotina já criada
    When excluir o plano de treino
    Then o plano é removido do sistema