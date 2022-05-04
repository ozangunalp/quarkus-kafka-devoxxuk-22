package com.ozangunalp;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.utils.Bytes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueStore;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.resteasy.reactive.RestStreamElementType;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;

@ApplicationScoped
@Path("/clicks-count")
public class ClicksAggregation {

    public static final String CLICKS_TOPIC = "clicks-persisted";
    public static final String CLICKS_PER_ELEMENT_TOPIC = "clicks-per-element";

    @Produces
    public Topology topology() {
        StreamsBuilder builder = new StreamsBuilder();
        Serde<PointerEventDTO> pointerEventSerde = Serdes.serdeFrom(new PointerEventSerializer(), new PointerEventDeserializer());
        builder.stream(CLICKS_TOPIC, Consumed.with(Serdes.String(), pointerEventSerde))
                .groupBy((s, event) -> event.xpath, Grouped.with(Serdes.String(), pointerEventSerde))
                .count(Materialized.<String, Long, KeyValueStore<Bytes, byte[]>>as("clicks-per-element-store")
                        .withCachingDisabled())
                .toStream()
                .to(CLICKS_PER_ELEMENT_TOPIC, Produced.with(Serdes.String(), Serdes.Long()));

        return builder.build();
    }

    @Channel(CLICKS_PER_ELEMENT_TOPIC)
    Multi<KafkaRecord<String, Long>> clickCount;

    @GET
    @javax.ws.rs.Produces(MediaType.SERVER_SENT_EVENTS)
    @RestStreamElementType(MediaType.APPLICATION_JSON)
    public Multi<Tuple2<String, Long>> stream() {
        return clickCount.onItem()
                .transformToUniAndConcatenate(r -> Uni.createFrom().completionStage(r.ack())
                .replaceWith(Tuple2.of(r.getKey(), r.getPayload())));
    }
}
