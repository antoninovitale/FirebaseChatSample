package com.fleetmatics.chat.model;

/**
 * Created by antoninovitale 17/07/15.
 * Copyright © 2015. Fleetmatics Development Limited. All rights reserved.
 **/
public enum UserStatus {
    UNKNOWN(0), LOW_BATTERY(1), DRIVING(2), MEETING(3), FREE(4);

    private int value;

    UserStatus(int value) {
        try {
            setValue(value);
        } catch (Exception e) {
            setValue(-1);
        }
    }

    public static UserStatus fromValue(int value) {
        if (values() != null && values().length > 0) {
            for (UserStatus userStatus : values()) {
                if (userStatus.getValue() == value)
                    return userStatus;
            }
        }
        return UNKNOWN;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
