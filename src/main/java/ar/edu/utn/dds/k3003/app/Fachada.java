package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.facades.FachadaHeladeras;
import ar.edu.utn.dds.k3003.facades.FachadaLogistica;
import ar.edu.utn.dds.k3003.facades.FachadaViandas;
import ar.edu.utn.dds.k3003.facades.dtos.*;
import ar.edu.utn.dds.k3003.facades.exceptions.TrasladoNoAsignableException;
import ar.edu.utn.dds.k3003.model.Ruta;
import ar.edu.utn.dds.k3003.model.Traslado;
import ar.edu.utn.dds.k3003.repositorios.RepositorioRuta;
import ar.edu.utn.dds.k3003.repositorios.RepositorioTraslado;
import ar.edu.utn.dds.k3003.repositorios.RutaMapper;
import ar.edu.utn.dds.k3003.repositorios.TrasladoMapper;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

public class Fachada implements FachadaLogistica {


    private final RepositorioRuta repositorioRuta;

    private final RepositorioTraslado repositorioTraslado;

    private final RutaMapper rutaMapper;

    private final TrasladoMapper trasladoMapper;
    private FachadaViandas fachadaViandas;
    private FachadaHeladeras fachadaHeladeras;

    public Fachada(){
        this.repositorioRuta = new RepositorioRuta();
        this.rutaMapper = new RutaMapper();
        this.repositorioTraslado = new RepositorioTraslado();
        this.trasladoMapper = new TrasladoMapper();
    }
@Override
public RutaDTO agregar(RutaDTO rutaDTO){
Ruta ruta = new Ruta(rutaDTO.getColaboradorId(), rutaDTO.getHeladeraIdOrigen(), rutaDTO.getHeladeraIdDestino());
ruta = this.repositorioRuta.guardar(ruta);
return rutaMapper.mapear(ruta);
}

@Override
    public TrasladoDTO buscarXId(Long trasladoID) throws NoSuchElementException{
        return trasladoMapper.mapear(repositorioTraslado.buscarXId(trasladoID));
}

    public  RutaDTO buscarRutaXId(Long rutaID) throws NoSuchElementException{
        return rutaMapper.mapear(repositorioRuta.buscarXId(rutaID));
    }


@Override
    public TrasladoDTO asignarTraslado(TrasladoDTO trasladoDTO) throws TrasladoNoAsignableException{

    ViandaDTO viandaDTO = fachadaViandas.buscarXQR(trasladoDTO.getQrVianda());
    Random random = new Random();
    List<Ruta> rutasPosibles = repositorioRuta.buscarXHeladera(trasladoDTO.getHeladeraOrigen(), trasladoDTO.getHeladeraDestino());
    if(rutasPosibles.isEmpty()){
        throw new TrasladoNoAsignableException("No se encontraron rutas posibles para el traslado");
    }
    Ruta ruta = rutasPosibles.get(random.nextInt(rutasPosibles.size()));

    Traslado traslado = new Traslado(viandaDTO.getCodigoQR(), EstadoTrasladoEnum.ASIGNADO, trasladoDTO.getFechaTraslado(), ruta);
    repositorioTraslado.guardar(traslado);
    return trasladoMapper.mapear(traslado);

    }

@Override
    public List<TrasladoDTO> trasladosDeColaborador(Long colaboradorId, Integer mes, Integer anio){
        List<Traslado> traslados = repositorioTraslado.buscarXColaborador(colaboradorId);
        List<Traslado> trasladosFiltrados = traslados.stream().filter(t -> t.getFechaTraslado().getMonthValue() == mes &&
                                                                        t.getFechaTraslado().getYear() == anio).toList();
        return trasladosFiltrados.stream().map(trasladoMapper :: mapear).toList();
}

@Override
    public void trasladoRetirado(Long trasladoId){
        Traslado traslado = repositorioTraslado.buscarXId(trasladoId);
        RetiroDTO retiro = new RetiroDTO(traslado.getQrVianda(), "321", traslado.getRuta().getHeladeraOrigen());
        fachadaHeladeras.retirar(retiro);
        fachadaViandas.modificarEstado(traslado.getQrVianda(), EstadoViandaEnum.EN_TRASLADO);
        repositorioTraslado.guardar(new Traslado(traslado.getQrVianda(), EstadoTrasladoEnum.EN_VIAJE, traslado.getFechaTraslado(), traslado.getRuta()));
}

@Override
    public void trasladoDepositado(Long trasladoId){
    Traslado traslado = repositorioTraslado.buscarXId(trasladoId);
    fachadaHeladeras.depositar(traslado.getRuta().getHeladeraDestino(), traslado.getQrVianda());
    fachadaViandas.modificarHeladera(traslado.getQrVianda(),traslado.getRuta().getHeladeraDestino());
    fachadaViandas.modificarEstado(traslado.getQrVianda(), EstadoViandaEnum.DEPOSITADA);
    repositorioTraslado.guardar(new Traslado(traslado.getQrVianda(), EstadoTrasladoEnum.ENTREGADO, traslado.getFechaTraslado(), traslado.getRuta()));
}

    public List<RutaDTO> rutas(){
        return repositorioRuta.todos().stream().map(rutaMapper::mapear).toList();
    }

    public List<TrasladoDTO> traslados(){
        return repositorioTraslado.todos().stream().map(trasladoMapper::mapear).toList();
    }

    public void modificarEstadoTraslado(Long trasladoId, EstadoTrasladoEnum nuevoEstado) throws NoSuchElementException{
        repositorioTraslado.modificarEstadoXID(trasladoId, nuevoEstado);
    }


@Override
public void setHeladerasProxy(FachadaHeladeras fachadaHeladeras){this.fachadaHeladeras = fachadaHeladeras;}
@Override
public void setViandasProxy(FachadaViandas fachadaViandas){this.fachadaViandas = fachadaViandas;}

}
