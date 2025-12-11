package Up.Power.aplicacao.conquista;

// Interface para ser implementada por todas as classes de avaliação de conquistas.
public interface ConquistaObserver {
    /**
     * Chamado pelo Subject (ConquistaServicoAplicacao) quando um evento importante
     * que pode desbloquear uma conquista acontece.
     * * @param eventType Uma String ou Enum que identifica o tipo de evento (ex: "EXERCICIO_CONCLUIDO").
     * @param eventData Dados relevantes ao evento (ex: IDs, contadores, etc.).
     */
    void update(String eventType, Object eventData);
}