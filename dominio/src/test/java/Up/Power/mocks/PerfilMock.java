package Up.Power.mocks;


import Up.Power.Perfil;
import Up.Power.perfil.PerfilId;
import Up.Power.perfil.PerfilRepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PerfilMock implements PerfilRepository {

    private final Map<PerfilId, Perfil> bancoEmMemoria;

    public PerfilMock() {
        this.bancoEmMemoria = new HashMap<>();
    }

    @Override
    public Perfil save(Perfil perfil) {
        if (perfil == null || perfil.getId() == null) {
            throw new IllegalArgumentException("Perfil e ID do Perfil não podem ser nulos.");
        }
        bancoEmMemoria.put(perfil.getId(), perfil);
        return perfil;
    }

    @Override
    public Optional<Perfil> findById(PerfilId id) {
        return Optional.ofNullable(bancoEmMemoria.get(id));
    }

    public void deleteAll() {
        bancoEmMemoria.clear();
    }

    @Override
    public boolean existsAmizade(PerfilId perfilId1, PerfilId perfilId2) {
        // Encontra os perfis no nosso "banco" em memória
        Perfil perfil1 = bancoEmMemoria.get(perfilId1);
        Perfil perfil2 = bancoEmMemoria.get(perfilId2);

        if (perfil1 == null || perfil2 == null) {
            return false; // Se um dos perfis não existe, eles não podem ser amigos
        }

        // Simula a busca do banco: verifica se o email de perfil2 está na lista de amigos de perfil1
        boolean amigoEmUmaDirecao = perfil1.getAmigos().stream()
                .anyMatch(amigo -> amigo.getUsuarioEmail().equals(perfil2.getUsuarioEmail()));

        // Em um sistema real, você poderia querer verificar a amizade nos dois sentidos
        boolean amigoNaOutraDirecao = perfil2.getAmigos().stream()
                .anyMatch(amigo -> amigo.getUsuarioEmail().equals(perfil1.getUsuarioEmail()));

        return amigoEmUmaDirecao || amigoNaOutraDirecao;
    }
}