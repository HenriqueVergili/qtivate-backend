package com.qtivate.server.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Class {
    private String classId;
    private Date start;
    private Date end;
    private List<String> tokens;
    private ArrayList<String> present;

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }

    public List<String> getPresent() {
        return present;
    }

    public void setPresent(ArrayList<String> present) {
        this.present = present;
    }
}
