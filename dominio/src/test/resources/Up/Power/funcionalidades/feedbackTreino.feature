Feature: Feedback do Treino

  Scenario: Conseguir adicionar um feedback
    Given que o usuário tenha feito um treino
    When um usuário tentar adicionar uma descrição para o treino
    Then o sistema informa que o treino possui uma descrição

  Scenario: Conseguir editar um feedback
    Given que um usuário possua uma descrição em um treino
    When um usuário optar por editar essa descrição
    Then o sistema informa que o treino possui essa nova descrição