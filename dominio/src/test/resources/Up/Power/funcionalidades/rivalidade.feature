Feature: Rivalidade

  Scenario: Conseguir adicionar uma rivalidade
    Given que o usuario possua uma amizade
    When um usuário tentar iniciar uma rivalidade e ser aceito
    Then  o sistema informa que a rivalidade comecou

  Scenario: Finalizar uma rivalidade
    Given que o usuário possua uma rivalidade ativa
    When um usuário optar por finalizar uma rivalidade
    Then o sistema informa que a rivalidade finalizou

  Scenario: Nao  conseguir iniciar  uma rivalidade
    Given que o usuário possua uma amizade que já está em uma rivalidade
    When um usuário optar por iniciar uma rivalidade
    Then o sistema informa que o usuario nao esta disponivel pois já está em uma rivalidade