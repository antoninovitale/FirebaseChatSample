package com.fleetmatics.chat.model;

import java.util.HashMap;

/**
 * Created by antoninovitale 17/07/15.
 * Copyright © 2015. Fleetmatics Development Limited. All rights reserved.
 **/
public class Room {
    private String roomId;
    private RoomType roomType;
    private RoomStatus roomStatus;
    private HashMap<String, ChatMessage> messages;
//    private List<ChatMessage> messages;

    public Room() {
    }

    public Room(String roomId, RoomType roomType, RoomStatus roomStatus) {
        this.roomId = roomId;
        this.roomType = roomType;
        this.roomStatus = roomStatus;
    }

    public String getRoomId() {
        return roomId;
    }

    public RoomType getRoomType() {
        return roomType;
    }

    public RoomStatus getRoomStatus() {
        return roomStatus;
    }

    public HashMap<String, ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(HashMap<String, ChatMessage> messages) {
        this.messages = messages;
    }
}
