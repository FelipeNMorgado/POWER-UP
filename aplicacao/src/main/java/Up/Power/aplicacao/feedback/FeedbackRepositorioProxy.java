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

    @Override
    public Feedback criar(Feedback feedback) {
        System.out.println("[Proxy] Criando feedback");
        long inicio = System.currentTimeMillis();

        var resultado = real.criar(feedback);

        long fim = System.currentTimeMillis();
        System.out.println("[Proxy] Tempo da criação: " + (fim - inicio) + "ms");

        return resultado;
    }

    @Override
    public Feedback modificar(Feedback feedback) {
        System.out.println("[Proxy] Modificando feedback id: " + feedback.getId().getId());
        long inicio = System.currentTimeMillis();

        var resultado = real.modificar(feedback);

        long fim = System.currentTimeMillis();
        System.out.println("[Proxy] Tempo da modificação: " + (fim - inicio) + "ms");

        return resultado;
    }

    @Override
    public void excluir(Integer id) {
        System.out.println("[Proxy] Excluindo feedback id: " + id);
        long inicio = System.currentTimeMillis();

        real.excluir(id);

        long fim = System.currentTimeMillis();
        System.out.println("[Proxy] Tempo da exclusão: " + (fim - inicio) + "ms");
    }

}

