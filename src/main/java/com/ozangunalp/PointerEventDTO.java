package com.ozangunalp;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import io.quarkus.mongodb.panache.common.MongoEntity;

@MongoEntity(collection = "PointerEvent")
public class PointerEventDTO extends PanacheMongoEntity {

    public String userId;
    public String sessionId;
    public String pointerType;

    public String xpath;
    public int screenX;
    public int screenY;
    public int clientX;
    public int clientY;

    public String deviceType;

    public PointerEventDTO() {
    }

    public PointerEventDTO(String userId, String sessionId, String pointerType, String xpath, int screenX, int screenY, int clientX, int clientY, String deviceType) {
        this.userId = userId;
        this.sessionId = sessionId;
        this.pointerType = pointerType;
        this.xpath = xpath;
        this.screenX = screenX;
        this.screenY = screenY;
        this.clientX = clientX;
        this.clientY = clientY;
        this.deviceType = deviceType;
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
                ", deviceType=" + deviceType +
                '}';
    }

    public static PointerEventDTO toDto(PointerEvent event) {
        return new PointerEventDTO(
                event.getUserId(),
                event.getSessionId(),
                event.getPointerType(),
                event.getXpath(),
                event.getScreenX(),
                event.getScreenY(),
                event.getClientX(),
                event.getClientY(),
                event.getDeviceType());
    }
}
