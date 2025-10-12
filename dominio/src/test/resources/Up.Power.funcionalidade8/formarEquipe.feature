Feature: Formação de equipes

  Scenario: Criar nova equipe
    Given que o usuário esteja logado
    When ele criar uma equipe
    Then o sistema o define como líder
    And gera um código de convite para novos membros

  Scenario: Excluir equipe
    Given que o usuário seja o líder
    When ele solicitar a exclusão da equipe
    Then o sistema remove a equipe após confirmação

  Scenario: Editar informações da equipe
    Given que o usuário seja líder ou administrador
    When ele editar as informações da equipe
    Then o sistema atualiza os dados da equipe

  Scenario: Gerenciar membros e desafios
    Given que o usuário seja líder ou administrador
    When ele acessar o painel da equipe
    Then poderá adicionar, remover ou atribuir funções aos membros
    And configurar desafios e sua duração

  Scenario: Excendendo o número máximo de membros (20 membros)
    Given que a equipe tenha 20 membros
    When o líder tentar adicionar outro membro
    Then o sistema impede a ação
    And exibe a mensagem "A equipe atingiu o limite máximo de 20 membros"

  Scenario: Comparar desempenho entre membros
    Given que a equipe tenha mais de um membro
    When o sistema exibir o painel da equipe
    Then os membros poderão visualizar e comparar seus desempenhos