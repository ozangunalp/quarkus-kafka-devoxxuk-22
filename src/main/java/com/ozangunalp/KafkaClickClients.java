package com.ozangunalp;

import java.util.HashMap;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;

import io.quarkus.kafka.client.serialization.ObjectMapperSerializer;
import io.smallrye.common.annotation.Identifier;

@ApplicationScoped
public class KafkaClickClients {

    @Inject
    @Identifier("default-kafka-broker")
    Map<String, Object> kafkaConfig;

    @Produces
    KafkaProducer<String, PointerEvent> producerClient() {
        Map<String, Object> producerConfig = new HashMap<>(kafkaConfig);
        producerConfig.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        producerConfig.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, ObjectMapperSerializer.class.getName());
        return new KafkaProducer<>(producerConfig);
    }

    @Produces
    KafkaConsumer<String, PointerEvent> consumerClient() {
        Map<String, Object> consumerConfig = new HashMap<>(kafkaConfig);
        consumerConfig.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        consumerConfig.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, PointerEventDeserializer.class.getName());
        return new KafkaConsumer<>(consumerConfig);
    }

}
