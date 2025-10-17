Feature: Metas

  Scenario: Conseguir adicionar metas
    Given que o usuário ainda não tenha metas
    When ele criar uma meta e preencher todos os campos corretamente
    Then o sistema irá criar a meta com sucesso

  Scenario: Não conseguir finalizar as metas
    Given que eu não consiga concluir as metas no prazo
    When eu tentar coletar as recompensas
    Then elas não estarão disponíveis para serem coletadas
