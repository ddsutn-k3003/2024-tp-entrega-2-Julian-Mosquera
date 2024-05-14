package ar.edu.utn.dds.k3003.app;

import ar.edu.utn.dds.k3003.Clientes.ViandasProxy;
import ar.edu.utn.dds.k3003.Controllers.*;
import ar.edu.utn.dds.k3003.facades.dtos.Constants;
import ar.edu.utn.dds.k3003.repositorios.RepositorioRuta;
import ar.edu.utn.dds.k3003.repositorios.RepositorioTraslado;
import ar.edu.utn.dds.k3003.repositorios.RutaMapper;
import ar.edu.utn.dds.k3003.repositorios.TrasladoMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.javalin.Javalin;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class WebApp {

    String URL_VIANDAS;
    String URL_LOGISTICA;
    String URL_HELADERAS;
    String URL_COLABORADORES;


    public static void main(String[] args) {
        Javalin app = Javalin.create().start(8080);
        app.get("/", ctx -> ctx.result("HOLA MUNDO"));

        Fachada fachada = new Fachada();
        ObjectMapper objectMapper = createObjectMapper();
        fachada.setViandasProxy(new ViandasProxy(objectMapper));

        app.get("/rutas", new ListaRutasController(fachada));
        app.get("/rutas/{rutaID}", new BuscarRutaXIDController(fachada));
        app.post("/rutas", new AgregarRutasController(fachada));
        app.get("/traslados", new ListaTrasladosController(fachada));
        app.patch("/traslados/{trasladoId}", new modificarEstadoController(fachada));
        app.get("/traslados/{trasladoId}", new TrasladoXIdController(fachada));
        app.post("/traslados", new AgregarTrasladosController(fachada));

    }

    public static ObjectMapper createObjectMapper(){
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        var sdf = new SimpleDateFormat(Constants.DEFAULT_SERIALIZATION_FORMAT, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        objectMapper.setDateFormat(sdf);
        return objectMapper;
    }


}
