package com.ozangunalp;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class PointerEventDeserializer extends ObjectMapperDeserializer<PointerEvent> {
    public PointerEventDeserializer() {
        super(PointerEvent.class);
    }
}
