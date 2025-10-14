Feature: Conquistas

  Scenario: Obtendo conquista
    Given que o sistema possua conquistas ativas
    When um usuario alcançar os requisitos da conquista
    Then  o sistema informa que a conquista foi concluída e envia as recompensas para o usuário

  Scenario:  Escolher as badges
    Given que o usuário possua no mínimo uma conquista concluída
    When o usuário tentar escolher uma badge que representa essa conquista
    Then o sistema informa que essa badge foi escolhida e estabelece a vantagem específica

  Scenario: Não conseguir reivindicar uma conquista
    Given que o sistema possua conquistas ativas
    When  o usuário não conseguir alcançar os requisitos da conquista
    Then  o sistema informa que a conquista ainda não está disponível para conclusão
