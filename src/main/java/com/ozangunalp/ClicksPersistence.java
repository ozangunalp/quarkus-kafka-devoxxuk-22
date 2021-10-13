package com.ozangunalp;

import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Tuple;

@ApplicationScoped
public class ClicksPersistence {

    @Inject
    Logger log;

    @Inject
    PgPool pgClient;

    @Incoming("clicks")
    @Retry(delay = 200)
    public Uni<Void> clicks(List<PointerEvent> events) {
        log.infof("Processing %d events", events.size());
        return insertBatch(events);
    }

    private static final String sql =
            "INSERT INTO pointerevent (userid, sessionid, pointertype, xpath, screenx, screeny, clientx, clienty) " +
            "VALUES ($1, $2, $3, $4, $5, $6, $7, $8)";

    Uni<Void> insertBatch(List<PointerEvent> pointerEvents) {
        List<Tuple> tuples = pointerEvents.stream().map(PointerEvent::toTuple)
                .collect(Collectors.toList());
        if (pointerEvents.size() == 1) {
            return Uni.createFrom().voidItem().onItem()
                    .failWith(() -> new IllegalArgumentException(pointerEvents.get(0).toString()));
        }
        return pgClient.withTransaction(conn -> conn.preparedQuery(sql).executeBatch(tuples))
                .onItem().invoke(rows -> log.infof("Persisted %d clicks", pointerEvents.size()))
                .replaceWith(() -> null);
    }
}
