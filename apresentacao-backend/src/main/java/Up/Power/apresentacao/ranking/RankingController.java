package Up.Power.apresentacao.ranking;

import Up.Power.aplicacao.ranking.RankingEntrada;
import Up.Power.aplicacao.ranking.RankingServicoAplicacao;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/ranking")
public class RankingController {

    private final RankingServicoAplicacao rankingServicoAplicacao;

    public RankingController(RankingServicoAplicacao rankingServicoAplicacao) {
        this.rankingServicoAplicacao = rankingServicoAplicacao;
    }

    @GetMapping("/global")
    public ResponseEntity<List<RankingEntrada>> global() {
        try {
            return ResponseEntity.ok(rankingServicoAplicacao.rankingGlobal());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/amigos/{email}")
    public ResponseEntity<List<RankingEntrada>> amigos(@PathVariable("email") String email) {
        try {
            String emailDecodificado = URLDecoder.decode(email, StandardCharsets.UTF_8);
            return ResponseEntity.ok(rankingServicoAplicacao.rankingAmigos(emailDecodificado));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/equipe/{equipeId}")
    public ResponseEntity<List<RankingEntrada>> equipe(@PathVariable("equipeId") Integer equipeId) {
        try {
            return ResponseEntity.ok(rankingServicoAplicacao.rankingEquipe(equipeId));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

