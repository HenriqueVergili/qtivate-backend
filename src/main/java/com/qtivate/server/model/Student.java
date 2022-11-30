package com.qtivate.server.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;

@Document("students")
public class Student {
    @Id
    private String id;
    private String aid;
    private String name;

    public Student(@JsonProperty("aid") String aid,
                   @JsonProperty("name") String name) {
        this.aid = aid;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getAID() {
        return aid;
    }

    public String getName() {
        return name;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setAID(String aid) {
        this.aid = aid;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return this.id.equals(student.id) && this.aid.equals(student.aid) && this.name.equals(student.name);
    }

    @Override
    public int hashCode() {
        int ret = 666;
        ret = ret * 13 + this.id.hashCode();
        ret = ret * 13 + this.aid.hashCode();
        ret = ret * 13 + this.name.hashCode();
        return ret;
    }

    @Override
    public String toString() {
        return "{\n" +
                this.id + ",\n" +
                this.aid + ",\n" +
                this.name + "\n}";
    }

    public void changeAll(Student newStudent) {
        if (newStudent == null) return;
        if (newStudent.name != null) this.name = newStudent.name;
        if (newStudent.aid != null) this.aid = newStudent.aid;
    }
}