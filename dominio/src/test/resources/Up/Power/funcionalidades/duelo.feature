Feature: Duelos

  Scenario: Conseguir duelar com outro usuário amigo
    Given que um usuario não tenha feito um duelo contra o amigo desafidado na semana
    When um usuario tentar iniciar um duelo com outro usuário
    Then o sistema iniciara o duelo e fara o calculo do vencedor

  Scenario: Impedir duelo dentro do prazo de cooldown
    Given um usuário faca um desafio e o prazo entre um duelo e outro nao tiver acabado
    When um usario tentar desafiar outro
    Then o sistema informa que é impossível duelar no momento