package Up.Power.frequencia;

import Up.Power.Frequencia;
import Up.Power.perfil.PerfilId;
import Up.Power.planoTreino.PlanoTId;
import Up.Power.treino.TreinoId;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FrequenciaService {

    private final FrequenciaRepository repository;

    public FrequenciaService(FrequenciaRepository repository) {
        this.repository = repository;
    }

    public void registrarPresenca(PerfilId perfilId, TreinoId treinoId, PlanoTId planoTreinoId) {
        registrarPresencaComFoto(perfilId, treinoId, planoTreinoId, null);
    }

    public void registrarPresencaComFoto(PerfilId perfilId, TreinoId treinoId, PlanoTId planoTreinoId, String fotoBase64) {
        // Verificar se já existe frequência para hoje no mesmo plano
        LocalDate hoje = LocalDate.now();
        List<Frequencia> frequenciasHoje = repository.listarPorPerfilEData(perfilId.getId(), hoje);
        
        // Se já existe frequência para hoje no mesmo plano, não registrar novamente
        boolean jaRegistrado = frequenciasHoje.stream()
                .anyMatch(f -> f.getPlanoTId() != null && f.getPlanoTId().equals(planoTreinoId));
        
        if (jaRegistrado) {
            throw new IllegalStateException("Você já registrou presença hoje. Não é possível registrar novamente no mesmo dia.");
        }

        Frequencia frequencia = new Frequencia(
            new FrequenciaId(0), // Será gerado pelo banco
            perfilId, 
            treinoId, 
            LocalDateTime.now()
        );
        
        frequencia.setPlanoT(planoTreinoId);

        // Se houver foto, adiciona
        if (fotoBase64 != null && !fotoBase64.isBlank()) {
            frequencia.setFoto(fotoBase64);
        }

        repository.salvar(frequencia);
    }

    public void registrarAusencia(FrequenciaId frequenciaId) {
        Frequencia freq = repository.obterFrequencia(frequenciaId, LocalDateTime.now());
        if (freq != null) {
            // Poderia registrar a ausência explicitamente aqui, se quiser no futuro.
        }
    }

    public boolean usuarioFoiAoTreino(FrequenciaId frequenciaId) {
        return Optional.ofNullable(repository.obterFrequencia(frequenciaId, LocalDateTime.now()))
                .isPresent();
    }

    /**
     * Calcula a sequência de dias consecutivos de frequência
     */
    public int calcularSequenciaDias(PerfilId perfilId, PlanoTId planoTreinoId) {
        List<Frequencia> frequencias = repository.listarPorPerfilEPlanoTreino(perfilId.getId(), planoTreinoId.getId());
        
        if (frequencias.isEmpty()) {
            return 0;
        }

        // Ordenar por data (mais recente primeiro)
        List<LocalDate> datas = frequencias.stream()
                .map(f -> f.getDataDePresenca().toLocalDate())
                .distinct()
                .sorted((a, b) -> b.compareTo(a)) // Ordenar descendente
                .collect(Collectors.toList());

        if (datas.isEmpty()) {
            return 0;
        }

        // Verificar se a data mais recente é hoje ou ontem
        LocalDate hoje = LocalDate.now();
        LocalDate dataMaisRecente = datas.get(0);
        
        // Se a data mais recente não for hoje ou ontem, sequência é 0
        if (dataMaisRecente.isBefore(hoje.minusDays(1))) {
            return 0;
        }

        // Contar dias consecutivos
        int sequencia = 0;
        LocalDate dataEsperada = hoje;
        
        for (LocalDate data : datas) {
            if (data.equals(dataEsperada) || data.equals(dataEsperada.minusDays(1))) {
                sequencia++;
                dataEsperada = data.minusDays(1);
            } else {
                break;
            }
        }

        return sequencia;
    }

    /**
     * Calcula quantos dias de frequência foram registrados na semana atual
     */
    public int calcularFrequenciaSemanal(PerfilId perfilId, PlanoTId planoTreinoId) {
        LocalDate hoje = LocalDate.now();
        LocalDate inicioSemana = hoje.with(java.time.DayOfWeek.MONDAY);
        LocalDate fimSemana = inicioSemana.plusDays(6);

        List<Frequencia> frequencias = repository.listarPorPerfilEPlanoTreino(perfilId.getId(), planoTreinoId.getId());
        
        return (int) frequencias.stream()
                .map(f -> f.getDataDePresenca().toLocalDate())
                .filter(data -> !data.isBefore(inicioSemana) && !data.isAfter(fimSemana))
                .distinct()
                .count();
    }

    /**
     * Calcula a sequência de dias consecutivos considerando todas as frequências do perfil
     * (todos os planos de treino)
     */
    public int calcularSequenciaDiasTotal(PerfilId perfilId) {
        List<Frequencia> todasFrequencias = repository.listarPorPerfil(perfilId.getId());
        
        if (todasFrequencias.isEmpty()) {
            return 0;
        }

        // Obter todas as datas únicas (independente do plano)
        List<LocalDate> datas = todasFrequencias.stream()
                .map(f -> f.getDataDePresenca().toLocalDate())
                .distinct()
                .sorted((a, b) -> b.compareTo(a)) // Ordenar descendente (mais recente primeiro)
                .collect(Collectors.toList());

        if (datas.isEmpty()) {
            return 0;
        }

        LocalDate hoje = LocalDate.now();
        LocalDate dataMaisRecente = datas.get(0);
        
        // Se a data mais recente não for hoje ou ontem, sequência é 0
        if (dataMaisRecente.isBefore(hoje.minusDays(1))) {
            return 0;
        }

        // Contar dias consecutivos a partir de hoje
        int sequencia = 0;
        LocalDate dataEsperada = hoje;
        
        for (LocalDate data : datas) {
            if (data.equals(dataEsperada) || data.equals(dataEsperada.minusDays(1))) {
                sequencia++;
                dataEsperada = data.minusDays(1);
            } else {
                break;
            }
        }

        return sequencia;
    }
}
