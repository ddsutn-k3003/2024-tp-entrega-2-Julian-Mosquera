package ar.edu.utn.dds.k3003.repositorios;

import ar.edu.utn.dds.k3003.model.Ruta;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

public class RepositorioRuta {

    private List<Ruta> rutas;
    private AtomicLong id = new AtomicLong();

    public RepositorioRuta(){
        this.rutas = new ArrayList<>();
    }

    public Ruta guardar(Ruta ruta){
        if(Objects.isNull(ruta.getId())){
            ruta.setId(id.getAndIncrement());
            this.rutas.add(ruta);
        }
        return ruta;
    }

    public Ruta buscarXId(Long id){
        Optional<Ruta> idRes = this.rutas.stream().filter(r -> r.getId().equals(id)).findFirst();
        return idRes.orElseThrow(() -> new NoSuchElementException("No hay una ruta con el id:" + id));
    }

    public List<Ruta> buscarXHeladera(Integer heladeraOrigen, Integer heladeraDestino){
        return rutas.stream().filter(r -> r.getHeladeraOrigen().equals(heladeraOrigen) &&
                r.getHeladeraDestino().equals(heladeraDestino)).toList();
    }

    public List<Ruta> todos(){
        return this.rutas;
    }
}
