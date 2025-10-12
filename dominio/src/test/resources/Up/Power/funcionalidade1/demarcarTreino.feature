Feature: Organizar e demarcar frequência de treino

    Scenario: Conseguir classificar os dias de treino
        Given que o sistema possua um usuário criado
        When o usuário tentar escolher os dias que vai à academia
        Then o sistema informa os dias disponíveis para sua frequência

    Scenario: Conseguir demarcar frequência
        Given que o sistema possua um usuário criado
        When o usuário tentar demarcar sua frequência
        Then o sistema demarca que o usuário foi à academia
        And adiciona um dia a mais na contagem de treinos da semana

    Scenario: Não conseguir demarcar frequência
        Given que o sistema possua um usuário criado
        When o usuário não marcar presença em um dia específico
        Then o sistema informa que o usuário não foi à academia
        And zera a contagem de treinos em sequência