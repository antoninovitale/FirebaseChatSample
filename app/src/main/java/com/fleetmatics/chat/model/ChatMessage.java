package com.fleetmatics.chat.model;

import java.util.Date;

public class ChatMessage {
    private String message;
    private String author;
    private Date timestamp;

    // Required default constructor for Firebase object mapping
    public ChatMessage() {
    }

    public ChatMessage(String message, String author, Date timestamp) {
        this.message = message;
        this.author = author;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
