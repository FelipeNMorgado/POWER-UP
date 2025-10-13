Feature: Criar e modificar seu plano nutricional

  Scenario: Conseguir adicionar o plano nutricional
    Given que um usuario criou seu plano nutricional e esta tudo preenchido
    When o usuario tentar salva-lo
    Then o sistema ira salva-lo com sucesso, podendo ser vizualiza-lo quando desejar

  Scenario: Conseguir modificar o plano nutricional
    Given que um usuario tenha um plano nutricional salvo e deseja edita-lo
    When o usario fizer alteracoes no plano e confirmar as mudancas
    Then o sistema ira salvar as alteracoes feitas e mostrar a versao atualizada para o usario

  Scenario: Nao conseguir modificar o plano nutricional, campo vazio
    Given o usuario tem um plano nutricional salvo
    When o usauario tentar colocar campos obricatorios em branco
    Then o sistema nao salvara a tentativa de alteracao

  Scenario: Nao conseguir modificar um atributo no plano nutricional
    Given que o usuário possua um plano nutricional ativo
    When o usuario tentar modificar um campo especifico com valores nao esperados
    Then o sistema informa que nao foi possivel alterar o atributo