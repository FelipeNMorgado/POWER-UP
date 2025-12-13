package Up.Power.aplicacao.rivalidade;


import Up.Power.infraestrutura.persistencia.jpa.rivalidade.JpaRivalidadeRepository;
import Up.Power.infraestrutura.persistencia.jpa.rivalidade.RivalidadeJpa;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Repository
public class RivalidadeRepositorioAplicacaoImpl implements RivalidadeRepositorioAplicacao {

    private final JpaRivalidadeRepository jpa;

    public RivalidadeRepositorioAplicacaoImpl(JpaRivalidadeRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<RivalidadeResumo> obterPorId(Integer id) {
        return jpa.findById(id)
                .map(RivalidadeResumoAssembler::toResumoFromEntity);
    }

    @Override
    public List<RivalidadeResumo> listarPorPerfil(Integer perfilId) {
        List<RivalidadeJpa> list = jpa.findByPerfilParticipante(perfilId);
        return list.stream()
                .map(RivalidadeResumoAssembler::toResumoFromEntity)
                .collect(Collectors.toList());
    }
}
