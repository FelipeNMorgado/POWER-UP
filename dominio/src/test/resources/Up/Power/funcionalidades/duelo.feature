Feature: Duelos

  Scenario: Conseguir duelar com outro usuário amigo
    Given que um usuario "Desafiante" não tenha feito um duelo contra o amigo "Desafiado" na semana
    When o usuario "Desafiante" tentar iniciar um duelo contra o usuario "Desafiado"
    Then o sistema iniciara o duelo e fara o calculo do vencedor

  Scenario: Impedir duelo dentro do prazo de cooldown
    Given um usuário "Desafiante" teve um duelo com o amigo "Desafiado" a menos de uma semana
    When o usario "Desafiante" tentar desafiar o amigo "Desafiado" novamente
    Then o sistema informa que é impossível duelar no momento