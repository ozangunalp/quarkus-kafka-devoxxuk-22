package com.ozangunalp;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class PointerEventDeserializer extends ObjectMapperDeserializer<PointerEventDTO> {
    public PointerEventDeserializer() {
        super(PointerEventDTO.class);
    }
}
