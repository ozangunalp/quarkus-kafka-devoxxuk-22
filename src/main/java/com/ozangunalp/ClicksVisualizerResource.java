package com.ozangunalp;

import java.time.Duration;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.reactive.messaging.Channel;

import io.smallrye.mutiny.Multi;

@ApplicationScoped
@Path("/clicks")
public class ClicksVisualizerResource {

    @Channel("clicks")
    Multi<PointerEvent> clicks;

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<PointerEvent> stream() {
        return Multi.createBy().merging()
                .streams(clicks, emitAPeriodicPing());
    }

    Multi<PointerEvent> emitAPeriodicPing() {
        return Multi.createFrom().ticks().every(Duration.ofSeconds(10))
                .onItem().transform(x -> new PointerEvent());
    }
}
