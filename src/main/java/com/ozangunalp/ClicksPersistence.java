package com.ozangunalp;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;

@ApplicationScoped
public class ClicksPersistence {

    @Inject
    Logger log;

    @Incoming("clicks")
    public Uni<Void> clicks(PointerEvent click) {
        log.infof("Click received %s", click);
        return Panache.withTransaction(click::persist).replaceWith(() -> null);
    }
}
