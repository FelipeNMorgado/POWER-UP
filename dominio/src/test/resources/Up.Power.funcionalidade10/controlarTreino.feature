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
