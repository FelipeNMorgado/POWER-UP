package Up.Power.aplicacao.feedback;

import Up.Power.Feedback;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.util.List;

@Primary //Para garantir que o spring injete esse aqui e não o Real
@Repository
public class FeedbackRepositorioProxy implements FeedbackRepositorioAplicacao {

    private final FeedbackRepositorioReal real;

    public FeedbackRepositorioProxy(FeedbackRepositorioReal real) {
        this.real = real;
    }

    @Override
    public List<FeedbackResumo> listarPorUsuario(String email) {

        long inicio = System.currentTimeMillis();
        System.out.println("[Proxy] Buscando feedbacks do usuário: " + email);

        var lista = real.listarPorUsuario(email);

        long fim = System.currentTimeMillis();
        System.out.println("[Proxy] Tempo total da consulta: " + (fim - inicio) + "ms");

        return lista;
    }

    @Override
    public Feedback obter(Integer id) {
        System.out.println("[Proxy] Consultando feedback id: " + id);
        long inicio = System.currentTimeMillis();

        var resultado = real.obter(id);

        long fim = System.currentTimeMillis();
        System.out.println("[Proxy] Tempo da consulta: " + (fim - inicio) + "ms");

        return resultado;
    }

}

