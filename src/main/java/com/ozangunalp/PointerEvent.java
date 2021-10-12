package com.ozangunalp;

public class PointerEvent {

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
