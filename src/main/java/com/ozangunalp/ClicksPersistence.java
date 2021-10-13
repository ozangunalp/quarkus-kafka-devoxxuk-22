package com.ozangunalp;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import io.smallrye.mutiny.Multi;
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
    @Outgoing("clicks-ignored")
    public Multi<Message<PointerEvent>> clicks(Multi<Message<PointerEvent>> clicks) {
        return clicks.group().intoLists().of(20, Duration.ofSeconds(5))
                .onItem().transformToMultiAndConcatenate(messages ->
                        insertBatch(messages.stream().map(Message::getPayload).collect(Collectors.toList()))
                                .onFailure().retry().atMost(1)
                                .onFailure().recoverWithUni(throwable ->
                                        Uni.combine().all().unis(messages.stream()
                                                        .map(m -> Uni.createFrom().completionStage(m.nack(throwable)))
                                                        .collect(Collectors.toList()))
                                                .discardItems())
                                .onItem().transformToMulti(unused -> Multi.createFrom().iterable(messages)));
    }

    @Incoming("clicks-ignored")
    public void ignores(PointerEvent ignored) {

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
