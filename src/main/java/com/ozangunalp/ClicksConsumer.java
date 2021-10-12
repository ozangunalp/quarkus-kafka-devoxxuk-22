package com.ozangunalp;

import java.time.Duration;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.eclipse.microprofile.context.ManagedExecutor;

import io.quarkus.runtime.StartupEvent;

@ApplicationScoped
public class ClicksConsumer {

    @Inject
    ManagedExecutor executor;

    @Inject
    KafkaConsumer<String, PointerEvent> consumer;

    @Inject
    ClicksVisualizerResource visualizer;


    void onStartup(@Observes StartupEvent startupEvent) {
        executor.submit(() -> {
            consumer.subscribe(List.of("clicks"));
            while (true) {
                if (!consumer.subscription().isEmpty()) {
                    ConsumerRecords<String, PointerEvent> records = consumer.poll(Duration.ofMillis(2000));
                    for (ConsumerRecord<String, PointerEvent> record : records) {
                        visualizer.sendEvent(record.topic(), record.value());
                    }
                }
                consumer.commitAsync();
            }
        });
    }
}
