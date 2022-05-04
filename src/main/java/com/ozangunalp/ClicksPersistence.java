package com.ozangunalp;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ClicksPersistence {

    @Inject
    Logger log;

    @Incoming("clicks")
    @Outgoing("clicks-persisted")
    @Transactional
    public Message<PointerEventDTO> clicks(Message<PointerEvent> message) {
        PointerEventDTO dto = persist(message.getPayload());
        return message.withPayload(dto);
    }

    PointerEventDTO persist(PointerEvent event) {
        PointerEventDTO dto = PointerEventDTO.toDto(event);
        dto.persistOrUpdate();
        log.infof("Persisted click %s", dto.id);
        return dto;
    }
}
