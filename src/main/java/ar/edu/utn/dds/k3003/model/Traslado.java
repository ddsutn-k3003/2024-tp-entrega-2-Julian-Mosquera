package ar.edu.utn.dds.k3003.model;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoTrasladoEnum;
import net.bytebuddy.asm.Advice;

import java.time.LocalDateTime;

public class Traslado {
    private Long id;
    private String qrVianda;
    private EstadoTrasladoEnum status;
    private LocalDateTime fechaTraslado;
    private Ruta ruta;
    private Long colaboradorId;


    public Traslado(String qrVianda, EstadoTrasladoEnum status, LocalDateTime fechaTraslado, Ruta ruta) {
        this.qrVianda = qrVianda;
        this.status = status;
        this.fechaTraslado = fechaTraslado;
        this.ruta = ruta;
        this.colaboradorId = ruta.getColaboradorId();
    }

    public Ruta getRuta() {
        return ruta;
    }

    public void setRuta(Ruta ruta) {
        this.ruta = ruta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQrVianda() {
        return qrVianda;
    }

    public void setQrVianda(String qrVianda) {
        this.qrVianda = qrVianda;
    }

    public EstadoTrasladoEnum getStatus() {
        return status;
    }

    public void setStatus(EstadoTrasladoEnum status) {
        this.status = status;
    }

    public LocalDateTime getFechaTraslado() {
        return fechaTraslado;
    }

    public void setFechaTraslado(LocalDateTime fechaTraslado) {
        this.fechaTraslado = fechaTraslado;
    }


    public Long getColaboradorId() {
        return colaboradorId;
    }

    public void setColaboradorId(Long colaboradorId) {
        this.colaboradorId = colaboradorId;
    }
}
