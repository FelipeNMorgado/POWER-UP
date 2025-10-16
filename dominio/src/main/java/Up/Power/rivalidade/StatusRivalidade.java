package Up.Power.rivalidade;

public enum StatusRivalidade {
    PENDENTE,   // O convite foi enviado e aguarda resposta.
    ATIVA,      // O convite foi aceito e a rivalidade começou.
    RECUSADA,   // O convite foi recusado.
    FINALIZADA, // A rivalidade terminou (por tempo ou por um vencedor).
    CANCELADA   // O usuário que enviou o convite o cancelou antes da resposta.
}