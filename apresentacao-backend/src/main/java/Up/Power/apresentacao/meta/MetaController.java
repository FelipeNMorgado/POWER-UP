package Up.Power.apresentacao.meta;

import Up.Power.aplicacao.meta.MetaServicoAplicacao;
import Up.Power.aplicacao.meta.MetaResumo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}

