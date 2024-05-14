package ar.edu.utn.dds.k3003.Clientes;

import ar.edu.utn.dds.k3003.facades.dtos.EstadoViandaEnum;
import ar.edu.utn.dds.k3003.facades.dtos.ViandaDTO;
import io.javalin.Javalin;

import java.time.LocalDateTime;

public class MockApp {
    public static void mockServer(){
        Javalin mockApp = Javalin.create().start(8081);

        mockApp.get("/viandas/{qr}", ctx -> {
            String qr = ctx.pathParam("qr");
            ViandaDTO vianda = new ViandaDTO(qr, LocalDateTime.now(), EstadoViandaEnum.PREPARADA, 1L, 1);
            ctx.json(vianda);
        });
        }
}
