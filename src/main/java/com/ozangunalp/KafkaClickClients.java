package com.ozangunalp;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import io.smallrye.common.annotation.Identifier;

@ApplicationScoped
public class KafkaClickClients {

    @Inject
    @Identifier("default-kafka-broker")
    Map<String, Object> kafkaConfig;

    @Produces
    KafkaConsumer<String, PointerEvent> consumerClient() {
        Map<String, Object> consumerConfig = new HashMap<>(kafkaConfig);
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, PointerEventDeserializer.class.getName());
        return new KafkaConsumer<>(consumerConfig);
    }

}
