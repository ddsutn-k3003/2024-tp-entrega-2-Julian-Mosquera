package ar.edu.utn.dds.k3003.Controllers;

import ar.edu.utn.dds.k3003.app.Fachada;
import ar.edu.utn.dds.k3003.facades.dtos.RutaDTO;
import ar.edu.utn.dds.k3003.model.Ruta;
import ar.edu.utn.dds.k3003.repositorios.RepositorioRuta;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.HttpStatus;
import org.jetbrains.annotations.NotNull;

public class AgregarRutasController implements Handler {

    private Fachada fachada;

    public AgregarRutasController(Fachada fachada){
        this.fachada = fachada;
    }


    @Override
    public void handle(@NotNull Context context) throws Exception {
        RutaDTO rutaDTO = context.bodyAsClass(RutaDTO.class);
        fachada.agregar(rutaDTO);
        context.status(HttpStatus.CREATED);
        context.result("Ruta agregada correctamente");
    }
}
