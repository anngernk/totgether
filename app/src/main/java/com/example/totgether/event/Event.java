package com.example.totgether.event;

public class Event {
    private int id;
    private String date;
    private String address;
    private String reason;

    public Event(int id, String date, String address, String reason) {
        this.id = id;
        this.date = date;
        this.address = address;
        this.reason = reason;
    }

    public int getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getAddress() {
        return address;
    }

    public String getReason() {
        return reason;
    }
}