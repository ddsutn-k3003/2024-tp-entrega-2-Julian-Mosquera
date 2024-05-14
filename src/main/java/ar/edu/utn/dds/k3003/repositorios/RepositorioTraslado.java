package ar.edu.utn.dds.k3003.repositorios;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoTrasladoEnum;
import ar.edu.utn.dds.k3003.facades.dtos.TrasladoDTO;
import ar.edu.utn.dds.k3003.model.Traslado;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class RepositorioTraslado {
    private List<Traslado> traslados;
    private AtomicLong id = new AtomicLong();

    public RepositorioTraslado(){
        this.traslados = new ArrayList<>();
    }

    public Traslado guardar(Traslado traslado){
        if(Objects.isNull(traslado.getId())){
            traslado.setId(id.getAndIncrement());
            this.traslados.add(traslado);
        }
        return traslado;
    }

    public Traslado buscarXId(Long id){
        Optional<Traslado> idTras = this.traslados.stream().filter(t -> t.getId().equals(id)).findFirst();
        return idTras.orElseThrow(() -> new NoSuchElementException("No hay un traslado con el id:" + id));
    }

    public List<Traslado> buscarXColaborador (Long colaboradorId){
        List<Traslado> traslColab = traslados.stream().filter(t -> t.getColaboradorId().equals(colaboradorId)).toList();
        if(traslColab.isEmpty()){
            throw new NoSuchElementException("El colaborador con Id:" + colaboradorId + "No tiene ningun traslado asignado");
        }
        return traslColab;
    }

    public List<Traslado> todos(){
        return this.traslados;
    }

    public void modificarEstadoXID(Long trasladoId, EstadoTrasladoEnum nuevoEstado) throws NoSuchElementException {
        Traslado trasladoAModificar = this.buscarXId(trasladoId);
        trasladoAModificar.setStatus(nuevoEstado);
        int indice = traslados.indexOf(trasladoAModificar);
        traslados.set(indice, trasladoAModificar);
    }


}
