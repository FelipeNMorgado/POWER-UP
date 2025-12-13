package Up.Power.aplicacao.rivalidade.template;

import org.springframework.transaction.annotation.Transactional;

import Up.Power.Rivalidade;
import Up.Power.aplicacao.rivalidade.RivalidadeResumo;
import Up.Power.aplicacao.rivalidade.RivalidadeResumoAssembler;
import Up.Power.rivalidade.RivalidadeService;

public abstract class OperacaoRivalidadeTemplate {

    protected final RivalidadeService dominioService;

    protected OperacaoRivalidadeTemplate(RivalidadeService dominioService) {
        this.dominioService = dominioService;
    }

    @Transactional
    public final RivalidadeResumo executarEnviarConvite(int perfil1Id, int perfil2Id, int exercicioId) {
        validarEnviarConvite(perfil1Id, perfil2Id, exercicioId);
        Rivalidade rivalidade = executarOperacaoEnviarConvite(perfil1Id, perfil2Id, exercicioId);
        return RivalidadeResumoAssembler.toResumo(rivalidade);
    }

    @Transactional
    public final RivalidadeResumo executarAceitar(int rivalidadeId, int usuarioId) {
        validarAceitar(rivalidadeId, usuarioId);
        Rivalidade rivalidade = executarOperacaoAceitar(rivalidadeId, usuarioId);
        return RivalidadeResumoAssembler.toResumo(rivalidade);
    }

    @Transactional
    public final RivalidadeResumo executarRecusar(int rivalidadeId, int usuarioId) {
        validarRecusar(rivalidadeId, usuarioId);
        Rivalidade rivalidade = executarOperacaoRecusar(rivalidadeId, usuarioId);
        return RivalidadeResumoAssembler.toResumo(rivalidade);
    }

    @Transactional
    public final RivalidadeResumo executarFinalizar(int rivalidadeId, int usuarioId) {
        validarFinalizar(rivalidadeId, usuarioId);
        Rivalidade rivalidade = executarOperacaoFinalizar(rivalidadeId, usuarioId);
        return RivalidadeResumoAssembler.toResumo(rivalidade);
    }

    @Transactional
    public final RivalidadeResumo executarCancelar(int rivalidadeId, int usuarioId) {
        validarCancelar(rivalidadeId, usuarioId);
        Rivalidade rivalidade = executarOperacaoCancelar(rivalidadeId, usuarioId);
        return RivalidadeResumoAssembler.toResumo(rivalidade);
    }

    protected void validarEnviarConvite(int perfil1Id, int perfil2Id, int exercicioId) {}
    protected void validarAceitar(int rivalidadeId, int usuarioId) {}
    protected void validarRecusar(int rivalidadeId, int usuarioId) {}
    protected void validarFinalizar(int rivalidadeId, int usuarioId) {}
    protected void validarCancelar(int rivalidadeId, int usuarioId) {}

    protected Rivalidade executarOperacaoEnviarConvite(int perfil1Id, int perfil2Id, int exercicioId) {
        throw new UnsupportedOperationException("Operação não suportada");
    }

    protected Rivalidade executarOperacaoAceitar(int rivalidadeId, int usuarioId) {
        throw new UnsupportedOperationException("Operação não suportada");
    }

    protected Rivalidade executarOperacaoRecusar(int rivalidadeId, int usuarioId) {
        throw new UnsupportedOperationException("Operação não suportada");
    }

    protected Rivalidade executarOperacaoFinalizar(int rivalidadeId, int usuarioId) {
        throw new UnsupportedOperationException("Operação não suportada");
    }

    protected Rivalidade executarOperacaoCancelar(int rivalidadeId, int usuarioId) {
        throw new UnsupportedOperationException("Operação não suportada");
    }
}
