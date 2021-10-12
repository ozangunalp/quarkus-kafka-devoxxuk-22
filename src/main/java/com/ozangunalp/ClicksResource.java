package com.ozangunalp;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jboss.logging.Logger;

@Path("/clicks")
public class ClicksResource {

    @Inject
    Logger log;

    @Inject
    KafkaProducer<String, PointerEvent> producer;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void postClick(@HeaderParam("user-agent") String userAgent, PointerEvent click) {
        log.infof("Click from %s : %s", userAgent, click);
        producer.send(new ProducerRecord<>("clicks", click.userId, click));
    }
}
