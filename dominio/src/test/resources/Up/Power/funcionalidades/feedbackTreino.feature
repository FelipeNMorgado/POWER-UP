Feature: Feedback do Treino

  Scenario: Conseguir adicionar um feedback
    Given que o usuário tenha feito um treino
    When um usuário tentar adicionar uma descrição "O melhor treino da minha vida" para o treino
    And escolher a categoria como "Excelente"
    Then o sistema informa que o treino possui a descrição "O melhor treino da minha vida"
    And a categoria "Excelente"

  Scenario: Conseguir editar um feedback
    Given que um usuário possua um treino com uma descrição "Não gostei muito do meu treino" em um treino com categoria "ComDor"
    When um usuário optar por editar essa descrição para "Até que gostei do treino de hoje"
    And editar categoria para "Bom"
    Then o sistema informa que o treino possui essa nova descrição e categoria

  Scenario: Conseguir excluir um feedback
    Given que um usuário possua uma descrição em um treino
    When um usuário optar por excluir essa descrição
    Then o sistema informa que o treino deixou de ter a descrição anterior

  Scenario: Não conseguir adicionar mais de um feedback
    Given que um usuário já tenha um feedback em um treino
    When um usuário optar por adicionar outro feedback a esse mesmo treino
    Then o sistema informa que não é possível adicionar mais de um feedback por treino
