package com.fleetmatics.chat.model;

import android.text.TextUtils;

/**
 * Created by antoninovitale 17/07/15.
 * Copyright © 2015. Fleetmatics Development Limited. All rights reserved.
 **/
public enum RoomType {
    UNKNOWN("Unknown"), PRIVATE("Private"), PUBLIC("Public");

    private String value;

    RoomType(String value) {
        setValue(value);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public RoomType fromValue(String value) {
        if (!TextUtils.isEmpty(value)) {
            if (value.equalsIgnoreCase(PRIVATE.getValue()))
                return PRIVATE;
            if (value.equalsIgnoreCase(PUBLIC.getValue()))
                return PUBLIC;
        }
        return UNKNOWN;
    }

}
