package com.fleetmatics.chat.model;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by antoninovitale 07/07/15.
 * Copyright © 2015. Fleetmatics Development Limited. All rights reserved.
 **/
public class User {
    private String uid;
    private String username;
    private String name = "";
    private String profession = "";
    private String address = "";
    private String photo = "";
    private Date lastOnline;
    private int userStatus = UserStatus.UNKNOWN.getValue();
    private HashMap<String, Room> rooms;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    public User() {
    }

    public User(String uid, String username) {
        this.uid = uid;
        this.username = username;
    }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserStatus getUserStatus() {
        return UserStatus.fromValue(userStatus);
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus.getValue();
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Date getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(Date lastOnline) {
        this.lastOnline = lastOnline;
    }

    public HashMap<String, Room> getRooms() {
        return rooms;
    }

    public void setRooms(HashMap<String, Room> rooms) {
        this.rooms = rooms;
    }

}