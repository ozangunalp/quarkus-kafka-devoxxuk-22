package com.ozangunalp;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import io.quarkus.cache.CacheResult;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.MutinyEmitter;

@Path("/clicks")
public class ClicksResource {

    @Inject
    Logger log;

    @Channel("clicks-out")
    MutinyEmitter<PointerEvent> emitter;

    @ConfigProperty(name = "api.key")
    String apiKey;

    @RestClient
    UserAgentService userAgentService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Void> postClick(@HeaderParam("user-agent") String userAgent, PointerEvent click) {
        log.infof("Click from %s : %s", userAgent, click);
        return parseUserAgent(userAgent)
                .onItem().transformToUni(ua -> {
                    click.deviceType = ua.device.type;
                    return emitter.send(click);
                });
    }

    @CacheResult(cacheName = "user-agent")
    Uni<UserAgent> parseUserAgent(String uaString) {
        return userAgentService.parse(uaString, apiKey).log();
    }
}
