package com.ozangunalp;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.OutboundSseEvent;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseBroadcaster;
import javax.ws.rs.sse.SseEventSink;

@ApplicationScoped
@Path("/clicks")
public class ClicksVisualizerResource {

    private final OutboundSseEvent.Builder eventBuilder;
    private final SseBroadcaster broadcaster;

    public ClicksVisualizerResource(@Context Sse sse) {
        this.eventBuilder = sse.newEventBuilder();
        this.broadcaster = sse.newBroadcaster();
    }

    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public void stream(@Context SseEventSink eventSink) {
        broadcaster.register(eventSink);
    }

    public void sendEvent(String topic, PointerEvent event) {
        this.eventBuilder
                .id(event.userId)
                .data(PointerEvent.class, event)
                .name(topic)
                .mediaType(MediaType.APPLICATION_JSON_TYPE)
                .reconnectDelay(3000);
        OutboundSseEvent sseEvent = eventBuilder.build();
        broadcaster.broadcast(sseEvent);
    }
}
