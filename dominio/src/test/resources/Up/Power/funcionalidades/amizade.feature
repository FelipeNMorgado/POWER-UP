Feature: Adição de amigos

  Scenario: Conseguir adicionar um amigo
    Given que o sistema possua mais de dois usuários disponíveis
    When um usuário enviar um convite de amizade
    And o usuário que recebeu o convite aceita
    Then o sistema informa que os usuários são amigos

  Scenario: Conseguir remover um amigo
    Given que um usuário possua no mínimo uma amizade
    When um usuário optar por remover amizade
    Then o sistema informa que os usuários não são mais amigos

  Scenario: Pedido de amizade recusado
    Given que o sistema possua mais de dois usuários disponíveis
    When um usuário enviar um convite de amizade
    And o usuário que recebeu o convite recusa
    Then o sistema informa que os usuários não são amigos

