package com.qtivate.server.model;

import java.util.Date;
import java.util.List;

public class Meeting {
    private Date day;
    private List<Class> classes;

    public Meeting(Date day, List<Class> classes) {
        this.day = day;
        this.classes = classes;
    }

    public Date getDay() {
        return day;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public List<Class> getClasses() {
        return classes;
    }

    public void setClasses(List<Class> classes) {
        this.classes = classes;
    }
}
