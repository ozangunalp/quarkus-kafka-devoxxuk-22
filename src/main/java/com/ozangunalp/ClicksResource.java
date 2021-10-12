package com.ozangunalp;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.logging.Logger;

import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;

@Path("/clicks")
public class ClicksResource {

    @Inject
    Logger log;

    @Channel("clicks-out")
    MutinyEmitter<PointerEvent> emitter;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Void> postClick(@HeaderParam("user-agent") String userAgent, PointerEvent click) {
        log.infof("Click from %s : %s", userAgent, click);
        return emitter.send(click);
    }
}
