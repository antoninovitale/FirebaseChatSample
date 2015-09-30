package com.fleetmatics.chat.model;

import android.text.TextUtils;

/**
 * Created by antoninovitale 17/07/15.
 * Copyright © 2015. Fleetmatics Development Limited. All rights reserved.
 **/
public enum RoomStatus {
    UNKNOWN("Unknown"), DELETED("Deleted"), ACTIVE("Active");

    private String value;

    RoomStatus(String value) {
        setValue(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public RoomStatus fromValue(String value) {
        if (!TextUtils.isEmpty(value)) {
            if (value.equalsIgnoreCase(DELETED.getValue()))
                return DELETED;
            if (value.equalsIgnoreCase(ACTIVE.getValue()))
                return ACTIVE;
        }
        return UNKNOWN;
    }

}
