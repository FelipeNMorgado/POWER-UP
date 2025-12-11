package Up.Power.apresentacao.meta;

import Up.Power.aplicacao.meta.MetaServicoAplicacao;
import Up.Power.aplicacao.meta.MetaResumo;
import Up.Power.aplicacao.meta.CriarMetaRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/metas")
public class MetaController {

    private final MetaServicoAplicacao metaServicoAplicacao;

    public MetaController(MetaServicoAplicacao metaServicoAplicacao) {
        this.metaServicoAplicacao = metaServicoAplicacao;
    }

    @GetMapping("/{id}")
    public ResponseEntity<MetaResumo> obterPorId(@PathVariable("id") Integer id) {
        try {
            MetaResumo meta = metaServicoAplicacao.obterPorId(id);
            if (meta == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(meta);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/usuario/{userId}")
    public ResponseEntity<List<MetaResumo>> obterMetasPorUsuario(@PathVariable("userId") Integer userId) {
        try {
            List<MetaResumo> metas = metaServicoAplicacao.obterMetasPorUsuario(userId);
            return ResponseEntity.ok(metas);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}/perfil/{perfilId}")
    public ResponseEntity<MetaResumo> obterPorIdComPerfil(@PathVariable("id") Integer id, @PathVariable("perfilId") Integer perfilId) {
        try {
            MetaResumo meta = metaServicoAplicacao.obterPorId(id, perfilId);
            if (meta == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(meta);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{metaId}/pode-coletar-recompensas")
    public ResponseEntity<Boolean> podeColetarRecompensas(@PathVariable("metaId") Integer metaId) {
        try {
            boolean podeColetar = metaServicoAplicacao.podeColetarRecompensas(metaId);
            return ResponseEntity.ok(podeColetar);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<MetaResumo> criar(@RequestBody CriarMetaRequestDTO request) {
        try {
            CriarMetaRequest criarRequest = toCriarMetaRequest(request);
            MetaResumo meta = metaServicoAplicacao.criar(criarRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(meta);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<MetaResumo> atualizar(@PathVariable("id") Integer id, @RequestBody CriarMetaRequestDTO request) {
        try {
            CriarMetaRequest atualizarRequest = toCriarMetaRequest(request);
            MetaResumo meta = metaServicoAplicacao.atualizar(id, atualizarRequest);
            return ResponseEntity.ok(meta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable("id") Integer id) {
        try {
            metaServicoAplicacao.deletar(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private CriarMetaRequest toCriarMetaRequest(CriarMetaRequestDTO dto) {
        try {
            SimpleDateFormat sdfISO = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
            
            Date dataInicio = null;
            Date dataFim = null;
            
            if (dto.dataInicio() != null && !dto.dataInicio().isEmpty()) {
                try {
                    // Tentar formato ISO primeiro
                    dataInicio = sdfISO.parse(dto.dataInicio());
                } catch (ParseException e) {
                    // Se falhar, tentar formato date simples
                    dataInicio = sdfDate.parse(dto.dataInicio());
                }
            }
            
            if (dto.dataFim() != null && !dto.dataFim().isEmpty()) {
                try {
                    // Tentar formato ISO primeiro
                    dataFim = sdfISO.parse(dto.dataFim());
                } catch (ParseException e) {
                    // Se falhar, tentar formato date simples
                    dataFim = sdfDate.parse(dto.dataFim());
                }
            }
            
            return new CriarMetaRequest(
                    dto.nome(),
                    dto.exercicioId(),
                    dto.treinoId(),
                    dataInicio,
                    dataFim,
                    dto.exigenciaMinima()
            );
        } catch (ParseException e) {
            throw new IllegalArgumentException("Formato de data inválido: " + e.getMessage(), e);
        }
    }

    // DTO para receber dados do frontend
    public record CriarMetaRequestDTO(
            String nome,
            Integer exercicioId,
            Integer treinoId,
            String dataInicio,
            String dataFim,
            Double exigenciaMinima
    ) {}
}

