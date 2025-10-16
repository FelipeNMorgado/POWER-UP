package Up.Power;

import Up.Power.loja.LojaId;

import java.util.ArrayList;
import java.util.List;

public class Loja {
    private LojaId id;
    private List<Acessorio> acessorios;

    public Loja(LojaId id) {
        this.id = id;
        this.acessorios = new ArrayList<>();
    }

    public LojaId getId() { return id; }
    public List<Acessorio> getAcessorios() { return acessorios; }
}


