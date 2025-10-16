Feature: controleIntensidade

  Scenario: Ter informação dos meus recordes de levantamento de peso
    Given que um usuario ja tenh realizado um exercicio
    When ele pega o maior carga que ele ja pegou
    Then  o sistema atualiza o seu peso record e o parabeniza

  Scenario: Conseguir controlar minha progressão de Carga
    Given que um usuario já possua um plano de treino
    When evoluir nas minhas cargas e repetições
    Then ter uma visualização de desenvolvimento das cargas e registro delas