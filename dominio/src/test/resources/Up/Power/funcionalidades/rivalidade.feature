Feature: Rivalidade

  Scenario: Aceitar um convite de rivalidade com sucesso
    Given que "UsuarioUm" enviou um convite de rivalidade para "UsuarioDois"
    When "UsuarioDois" aceita o convite
    Then a rivalidade entre eles se torna ativa

  Scenario: Recusar um convite de rivalidade
    Given que "UsuarioUm" enviou um convite de rivalidade para "UsuarioDois"
    When "UsuarioDois" recusa o convite
    Then o status da rivalidade se torna "recusada"

  Scenario: Finalizar uma rivalidade
    Given que o usuário possua uma rivalidade ativa
    When um usuário optar por finalizar uma rivalidade
    Then o sistema informa que a rivalidade finalizou

  Scenario: Nao  conseguir iniciar  uma rivalidade
    Given que o usuário possua uma amizade que já está em uma rivalidade
    When um usuário optar por iniciar uma rivalidade
    Then o sistema informa que o usuario nao esta disponivel pois já está em uma rivalidade