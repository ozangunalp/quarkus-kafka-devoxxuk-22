package com.ozangunalp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.vertx.mutiny.sqlclient.Tuple;

@Entity
public class PointerEvent extends PanacheEntityBase {

    @Column(columnDefinition = "serial")
    @Id
    @GeneratedValue
    public Long id;

    public String userId;
    public String sessionId;
    public String pointerType;

    public String xpath;
    public int screenX;
    public int screenY;
    public int clientX;
    public int clientY;

    public PointerEvent() {
    }

    public Tuple toTuple() {
        return Tuple.from(new Object[]{userId, sessionId, pointerType, xpath, screenX, screenY, clientX, clientY});
    }

    @Override
    public String toString() {
        return "PointerEvent{" +
                "userId='" + userId + '\'' +
                ", sessionId='" + sessionId + '\'' +
                ", pointerType='" + pointerType + '\'' +
                ", xpath='" + xpath + '\'' +
                ", screenX=" + screenX +
                ", screenY=" + screenY +
                ", clientX=" + clientX +
                ", clientY=" + clientY +
                '}';
    }
}
