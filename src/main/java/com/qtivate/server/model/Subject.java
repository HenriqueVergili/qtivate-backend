package com.qtivate.server.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Document("subjects")
public class Subject {
    @Id
    private String id;
    private String subId;
    private String courseId;
    private String profId;
    private String name;
    private List<Student> students;
    private List<Meeting> meetings;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSubId() {
        return subId;
    }

    public void setSubId(String subId) {
        this.subId = subId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getProfId() {
        return profId;
    }

    public void setProfId(String profId) {
        this.profId = profId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Meeting> getMeetings() {
        return meetings;
    }

    public void setMeetings(List<Meeting> meetings) {
        this.meetings = meetings;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        if (!this.id.equals(subject.id) || !this.subId.equals(subject.subId) ||
                !this.name.equals(subject.name) || !this.profId.equals(subject.profId) ||
                !this.courseId.equals(subject.courseId) || (this.meetings.size() != subject.meetings.size()) ||
                (this.students.size() != subject.meetings.size()))
            return false;
        return this.meetings.containsAll(subject.meetings) && this.students.containsAll(subject.students);
    }
    @Override
    public int hashCode() {
        AtomicInteger ret = new AtomicInteger(666);
        ret.set(ret.get() * 13 + this.id.hashCode());
        ret.set(ret.get() * 13 + this.subId.hashCode());
        ret.set(ret.get() * 13 + this.name.hashCode());
        ret.set(ret.get() * 13 + this.courseId.hashCode());
        ret.set(ret.get() * 13 + this.profId.hashCode());

        this.meetings.forEach(metting -> {
            ret.set(ret.get() * 13 + metting.hashCode());
        });

        this.students.forEach(student -> {
            ret.set(ret.get() * 13 + student.hashCode());
        });

        return ret.get();
    }

    @Override
    public String toString() {
        AtomicReference<String> ret = new AtomicReference<>("{\n" +
                this.id + ",\n" +
                this.courseId + ",\n" +
                this.subId + ",\n" +
                this.profId + ",\n" +
                this.name + "\n}");
        this.meetings.forEach(metting -> {
            ret.set(ret.get() + metting.toString());
        });

        this.students.forEach(student -> {
            ret.set(ret.get() + student.toString());
        });

        return ret.get();
    }
}