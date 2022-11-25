package com.qtivate.server.model;

public class StudentPresence {
    private String aid;
    private String subId;
    private int count;
    private int total;
    private double presence;

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public double getPresence() {
        return presence;
    }

    public void setPresence(double presence) {
        this.presence = presence;
    }
}
