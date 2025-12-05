package Up.Power.aplicacao.duelo;

import Up.Power.duelo.Duelo;
import Up.Power.duelo.DueloId;

import java.util.Optional;

public interface DueloRepositorioAplicacao {
    Optional<Duelo> obterPorId(DueloId id);
    Optional<Duelo> ultimoEntrePerfis(Integer perfilId1, Integer perfilId2);
    Duelo salvar(Duelo duelo);
}