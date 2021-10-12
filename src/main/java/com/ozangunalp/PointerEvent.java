package com.ozangunalp;

import javax.persistence.Entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntity;

@Entity
public class PointerEvent extends PanacheEntity {

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
