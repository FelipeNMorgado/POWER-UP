package Up.Power.aplicacao.rivalidade;

import Up.Power.aplicacao.rivalidade.commands.*;
import Up.Power.aplicacao.rivalidade.operacoes.*;
import Up.Power.aplicacao.frequencia.FrequenciaServicoAplicacao;
import Up.Power.aplicacao.duelo.DueloRepositorioAplicacao;
import Up.Power.frequencia.FrequenciaRepository;
import Up.Power.avatar.AvatarRepository;
import Up.Power.perfil.PerfilId;
import Up.Power.perfil.PerfilRepository;
import Up.Power.Perfil;
import Up.Power.Duelo;
import Up.Power.avatar.AvatarId;
import Up.Power.Frequencia;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.DayOfWeek;
import java.util.List;
import java.util.Optional;

@Service
public class RivalidadeServicoAplicacao {

    private final EnviarConviteOperacao enviarConvite;
    private final AceitarRivalidadeOperacao aceitar;
    private final RecusarRivalidadeOperacao recusar;
    private final FinalizarRivalidadeOperacao finalizar;
    private final CancelarRivalidadeOperacao cancelar;
    private final RivalidadeRepositorioAplicacao rivalidadeRepositorioAplicacao;
    private final FrequenciaServicoAplicacao frequenciaServicoAplicacao;
    private final FrequenciaRepository frequenciaRepository;
    private final DueloRepositorioAplicacao dueloRepositorioAplicacao;
    private final AvatarRepository avatarRepository;
    private final PerfilRepository perfilRepository;

    public RivalidadeServicoAplicacao(
            EnviarConviteOperacao enviarConvite,
            AceitarRivalidadeOperacao aceitar,
            RecusarRivalidadeOperacao recusar,
            FinalizarRivalidadeOperacao finalizar,
            CancelarRivalidadeOperacao cancelar,
            RivalidadeRepositorioAplicacao rivalidadeRepositorioAplicacao,
            FrequenciaServicoAplicacao frequenciaServicoAplicacao,
            FrequenciaRepository frequenciaRepository,
            DueloRepositorioAplicacao dueloRepositorioAplicacao,
            AvatarRepository avatarRepository,
            PerfilRepository perfilRepository
    ) {
        this.enviarConvite = enviarConvite;
        this.aceitar = aceitar;
        this.recusar = recusar;
        this.finalizar = finalizar;
        this.cancelar = cancelar;
        this.rivalidadeRepositorioAplicacao = rivalidadeRepositorioAplicacao;
        this.frequenciaServicoAplicacao = frequenciaServicoAplicacao;
        this.frequenciaRepository = frequenciaRepository;
        this.dueloRepositorioAplicacao = dueloRepositorioAplicacao;
        this.avatarRepository = avatarRepository;
        this.perfilRepository = perfilRepository;
    }

    public RivalidadeResumo enviar(EnviarConviteCommand c) {
        return enriquecerComNomes(enviarConvite.executar(c));
    }

    public RivalidadeResumo aceitar(AceitarRivalidadeCommand c) {
        return enriquecerComNomes(aceitar.executar(c));
    }

    public RivalidadeResumo recusar(RecusarRivalidadeCommand c) {
        return enriquecerComNomes(recusar.executar(c));
    }

    public RivalidadeResumo finalizar(FinalizarRivalidadeCommand c) {
        return enriquecerComNomes(finalizar.executar(c));
    }

    public RivalidadeResumo cancelar(CancelarRivalidadeCommand c) {
        return enriquecerComNomes(cancelar.executar(c));
    }

    public List<RivalidadeResumo> listarPorPerfil(Integer perfilId) {
        List<RivalidadeResumo> rivalidades = rivalidadeRepositorioAplicacao.listarPorPerfil(perfilId);
        // Enriquecer com nomes dos perfis
        return rivalidades.stream()
                .map(r -> enriquecerComNomes(r))
                .toList();
    }

    private RivalidadeResumo enriquecerComNomes(RivalidadeResumo r) {
        String nomePerfil1 = null;
        String nomePerfil2 = null;
        
        if (r.perfil1() != null) {
            try {
                Perfil perfil1 = perfilRepository.findById(new PerfilId(r.perfil1())).orElse(null);
                if (perfil1 != null) {
                    nomePerfil1 = perfil1.getUsername();
                }
            } catch (Exception e) {
                System.err.println("Erro ao buscar nome do perfil1: " + e.getMessage());
            }
        }
        
        if (r.perfil2() != null) {
            try {
                Perfil perfil2 = perfilRepository.findById(new PerfilId(r.perfil2())).orElse(null);
                if (perfil2 != null) {
                    nomePerfil2 = perfil2.getUsername();
                }
            } catch (Exception e) {
                System.err.println("Erro ao buscar nome do perfil2: " + e.getMessage());
            }
        }
        
        return new RivalidadeResumo(
                r.id(),
                r.perfil1(),
                r.perfil2(),
                nomePerfil1,
                nomePerfil2,
                r.dataConvite(),
                r.inicio(),
                r.fim(),
                r.status()
        );
    }

    public ComparacaoRivalidade obterComparacao(Integer rivalidadeId, Integer perfilId) {
        RivalidadeResumo rivalidade = enriquecerComNomes(
                rivalidadeRepositorioAplicacao.obterPorId(rivalidadeId)
                        .orElseThrow(() -> new IllegalArgumentException("Rivalidade não encontrada"))
        );

        if (rivalidade.inicio() == null) {
            throw new IllegalStateException("A rivalidade ainda não foi aceita");
        }

        // Determinar qual perfil é o usuário e qual é o rival
        Integer perfilUsuario = perfilId;
        Integer perfilRival = rivalidade.perfil1().equals(perfilId) ? rivalidade.perfil2() : rivalidade.perfil1();

        LocalDate dataInicio = rivalidade.inicio().toLocalDate();

        // Calcular streak desde o início da rivalidade
        int streakUsuario = frequenciaServicoAplicacao.calcularSequenciaDiasDesdeData(perfilUsuario, dataInicio);
        int streakRival = frequenciaServicoAplicacao.calcularSequenciaDiasDesdeData(perfilRival, dataInicio);

        // Calcular treinos na semana atual
        LocalDate hoje = LocalDate.now();
        LocalDate inicioSemana = hoje.with(DayOfWeek.MONDAY);
        int treinosSemanaUsuario = calcularTreinosNaSemana(perfilUsuario, inicioSemana);
        int treinosSemanaRival = calcularTreinosNaSemana(perfilRival, inicioSemana);

        // Calcular duelos ganhos durante a rivalidade
        int duelosGanhosUsuario = calcularDuelosGanhos(perfilUsuario, perfilRival, rivalidade.inicio());
        int duelosGanhosRival = calcularDuelosGanhos(perfilRival, perfilUsuario, rivalidade.inicio());

        // Buscar informações dos perfis (nome e foto)
        Perfil perfilUsuarioObj = perfilRepository.findById(new PerfilId(perfilUsuario))
                .orElseThrow(() -> new IllegalArgumentException("Perfil do usuário não encontrado"));
        Perfil perfilRivalObj = perfilRepository.findById(new PerfilId(perfilRival))
                .orElseThrow(() -> new IllegalArgumentException("Perfil do rival não encontrado"));

        String nomeUsuario = perfilUsuarioObj.getUsername();
        String nomeRival = perfilRivalObj.getUsername();
        String fotoUsuario = perfilUsuarioObj.getFoto();
        String fotoRival = perfilRivalObj.getFoto();

        return new ComparacaoRivalidade(
                nomeUsuario,
                nomeRival,
                fotoUsuario,
                fotoRival,
                streakUsuario,
                streakRival,
                treinosSemanaUsuario,
                treinosSemanaRival,
                duelosGanhosUsuario,
                duelosGanhosRival
        );
    }

    private int calcularTreinosNaSemana(Integer perfilId, LocalDate inicioSemana) {
        LocalDate fimSemana = inicioSemana.plusDays(6);
        List<Frequencia> frequencias = frequenciaRepository.listarPorPerfil(perfilId);
        
        return (int) frequencias.stream()
                .map(f -> f.getDataDePresenca().toLocalDate())
                .filter(data -> !data.isBefore(inicioSemana) && !data.isAfter(fimSemana))
                .distinct()
                .count();
    }

    private int calcularDuelosGanhos(Integer perfilVencedor, Integer perfilRival, LocalDateTime dataInicio) {
        Optional<AvatarId> avatarVencedorOpt = avatarRepository.findByPerfilId(new PerfilId(perfilVencedor))
                .map(avatar -> avatar.getId());
        Optional<AvatarId> avatarRivalOpt = avatarRepository.findByPerfilId(new PerfilId(perfilRival))
                .map(avatar -> avatar.getId());

        if (avatarVencedorOpt.isEmpty() || avatarRivalOpt.isEmpty()) {
            return 0;
        }

        List<Duelo> duelos = dueloRepositorioAplicacao.findDuelsBetweenSince(
                avatarVencedorOpt.get(),
                avatarRivalOpt.get(),
                dataInicio
        );

        int vitorias = 0;
        for (Duelo duelo : duelos) {
            // Verificar se o perfil vencedor ganhou este duelo
            boolean vencedorEAvatar1 = duelo.getAvatar1().equals(avatarVencedorOpt.get());
            boolean vencedorEAvatar2 = duelo.getAvatar2().equals(avatarVencedorOpt.get());
            
            if (vencedorEAvatar1 && "VITORIA_DESAFIANTE(A1)".equals(duelo.getResultado())) {
                vitorias++;
            } else if (vencedorEAvatar2 && "VITORIA_DESAFIADO(A2)".equals(duelo.getResultado())) {
                vitorias++;
            }
        }

        return vitorias;
    }

    public record ComparacaoRivalidade(
            String nomeUsuario,
            String nomeRival,
            String fotoUsuario,
            String fotoRival,
            int streakUsuario,
            int streakRival,
            int treinosSemanaUsuario,
            int treinosSemanaRival,
            int duelosGanhosUsuario,
            int duelosGanhosRival
    ) {}
}
