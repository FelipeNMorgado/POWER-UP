package Up.Power.aplicacao.rivalidade;

import Up.Power.aplicacao.rivalidade.commands.*;
import Up.Power.aplicacao.rivalidade.operacoes.*;
import org.springframework.stereotype.Service;

@Service
public class RivalidadeServicoAplicacao {

    private final EnviarConviteOperacao enviarConvite;
    private final AceitarRivalidadeOperacao aceitar;
    private final RecusarRivalidadeOperacao recusar;
    private final FinalizarRivalidadeOperacao finalizar;

    public RivalidadeServicoAplicacao(
            EnviarConviteOperacao enviarConvite,
            AceitarRivalidadeOperacao aceitar,
            RecusarRivalidadeOperacao recusar,
            FinalizarRivalidadeOperacao finalizar
    ) {
        this.enviarConvite = enviarConvite;
        this.aceitar = aceitar;
        this.recusar = recusar;
        this.finalizar = finalizar;
    }

    public RivalidadeResumo enviar(EnviarConviteCommand c) {
        return enviarConvite.executar(c);
    }

    public RivalidadeResumo aceitar(AceitarRivalidadeCommand c) {
        return aceitar.executar(c);
    }

    public RivalidadeResumo recusar(RecusarRivalidadeCommand c) {
        return recusar.executar(c);
    }

    public RivalidadeResumo finalizar(FinalizarRivalidadeCommand c) {
        return finalizar.executar(c);
    }
}
