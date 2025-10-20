Feature: Progressão do Avatar (XP)

  Scenario: Avanço de níveis
    Given que o sistema possua um usuário ativo com um avatar no nível 1 e 50 de XP
    When o usuário receber 70 de XP
    Then o nível do avatar do usuário subirá para o nível 2

  Scenario: Comparação de atributos
    Given que o avatar de um usuário possui 100 pontos de força
    When o usuário progredir de carga e receber um bônus de 10 pontos de força
    Then o sistema informa que o atributo de força do seu avatar está em 110