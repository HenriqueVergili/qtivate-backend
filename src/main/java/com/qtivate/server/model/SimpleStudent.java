package com.qtivate.server.model;

import java.util.Objects;

public class SimpleStudent {
    private String aid;
    private String name;

    public SimpleStudent(String aid, String name) {
        this.aid = aid;
        this.name = name;
    }

    public SimpleStudent(Student student) {
        this.aid = student.getAID();
        this.name = student.getName();
    }

    public String getAid() {
        return aid;
    }

    public void setAid(String aid) {
        this.aid = aid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleStudent that = (SimpleStudent) o;

        if (!Objects.equals(aid, that.aid)) return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        int result = aid != null ? aid.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "SimpleStudent{" +
                "aid='" + aid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
