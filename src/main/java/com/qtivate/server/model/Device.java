package com.qtivate.server.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Calendar;
import java.util.Date;

@Document("devices")
public class Device {
    @Id
    private String id;

    private String mac;

    private Date limit;

    public Device(String mac, Date limit) {
        this.mac = mac;
        this.limit = limit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Date getLimit() {
        return limit;
    }

    public void setLimit(Date limit) {
        this.limit = limit;
    }

    public void changeLimit(int minutes) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, minutes);
        this.limit = calendar.getTime();
    }
}
