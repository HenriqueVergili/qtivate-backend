package com.qtivate.server.service;

import com.qtivate.server.exceptions.NotFoundException;
import com.qtivate.server.model.*;
import com.qtivate.server.model.Class;
import com.qtivate.server.respository.SubjectRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final TokenService tokenService;

    private final StudentService studentService;
    public SubjectService(SubjectRepository subjectRepository, TokenService tokenService, StudentService studentService) {
        this.subjectRepository = subjectRepository;
        this.tokenService = tokenService;
        this.studentService = studentService;
    }

    public boolean isTokenValid(String t) throws Exception {
        return tokenService.isTokenValid(t);
    }
    private Class findClassInMeetingsById(List<Meeting> meetings, String classId) {
        for(Meeting m: meetings) {
            for (Class c : m.getClasses()) {
                if (c.getClassId().equals(classId)) return c;
            }
        }
        return null;

    }

    // Add AID in present in class by classId
    public int addInPresenceByClassId(String classId, String studentAid) {
        Subject subject = subjectRepository.findSubjectByClassId(classId);
        if (subject == null) return 1;
        AtomicBoolean foundStudent = new AtomicBoolean(false);
        subject.getStudents().forEach(
                currentStudent -> {if (currentStudent.getAID().equals(studentAid)) foundStudent.set(true);}
        );
        if (!foundStudent.get()) return 3;
        AtomicInteger found = new AtomicInteger(2);
        subject.getMeetings().forEach(
                currentMeeting -> currentMeeting.getClasses().forEach(currentClass -> {
                    if (currentClass.getClassId().equals(classId)) {
                        if (!currentClass.getPresent().contains(studentAid)) {
                            currentClass.getPresent().add(studentAid);
                            found.set(0);
                        }
                    }
                }));
        subjectRepository.save(subject);
        return found.get();
    }


    // Set presence from presence list in class by classId
    public Map<String, String> setPresenceByClassId(String classId, List<String> studentAids) {
        Subject subject = subjectRepository.findSubjectByClassId(classId);
        List<String> addedPresence = new ArrayList<>();
        List<String> removedPresence = new ArrayList<>();
        subject.getMeetings().forEach(
                currentMeeting ->
                        currentMeeting.getClasses().forEach(
                                currentClass -> {
                                    if (currentClass.getClassId().equals(classId)) {

                                        studentAids.forEach(
                                                studentAid -> {
                                                    if (!currentClass.getPresent().contains(studentAid)){
                                                        currentClass.getPresent().add(studentAid);
                                                        addedPresence.add(studentAid);
                                                    }
                                                });
                                        currentClass.getPresent().forEach(
                                                present -> {
                                                    if (!studentAids.contains(present)) {
                                                        removedPresence.add(present);
                                                    }
                                                }
                                        );
                                        removedPresence.forEach(
                                                aid ->  currentClass.getPresent().remove(aid)
                                        );
                                    }
                                })
        );

        subjectRepository.save(subject);
        return Map.of("added", String.join(",",addedPresence), "removed", String.join(",",removedPresence));
    }

    public List<SimpleStudent> getAllStudentsBySubId(String subId) {
        List<SimpleStudent> response = new ArrayList<>();
        subjectRepository.findStudentsBySubId(subId).forEach(student -> {
            response.add(new SimpleStudent(student));
        });
        return response;
    }

    public List<SimpleStudent> getPresentsByClassId(String classId) {
        String result = subjectRepository.getPresencesByClassId(classId);
        if (result == null) throw new NotFoundException("classId not found");
        List<String> presenceList = List.of(result.split(","));
        List<Student> studentList = subjectRepository.findSubjectBySubId(classId.split("-")[0]).getStudents();
        List<SimpleStudent> response = new ArrayList<>();
        studentList.forEach(student -> {
            if (presenceList.contains(student.getAID())) response.add(new SimpleStudent(student));
        });
        return response;
    }

    // Add tokens in class by classId
    public int addTokensByClassId(String classId, String[] tokens) {
        Subject subject = subjectRepository.findSubjectByClassId(classId);
        if (subject == null) return 1;
        AtomicInteger found = new AtomicInteger(2);
        subject.getMeetings().forEach(
                currentMeeting -> currentMeeting.getClasses().forEach(currentClass -> {
                    if (currentClass.getClassId().equals(classId)) {
                        currentClass.setTokens(List.of(tokens));
                        found.set(0);
                    }
                }));
        subjectRepository.save(subject);
        return found.get();
    }

    // Get Presence by AID
    public List<StudentPresence> getPresenceByAID(String aid) {
        return subjectRepository.getStudentPresenceByAid(aid);
    }

    // Get subject name by Class ID

    public String getSubjectNameByClassId (String classId) throws Exception {
        Subject subject = subjectRepository.findSubjectByClassId(classId);
        if (subject == null) throw new Exception("Subject name not found");
        return subject.getName();
    }

    public int addStudentBySubId(String subId, String aid, String name) {
        Subject subject = subjectRepository.findSubjectBySubId(subId);
        Student newStudent = new Student(aid, name);
        subject.getStudents().add(newStudent);
        subject.setStudents(subject.getStudents());
        subjectRepository.save(subject);
        return 0;
    }
}
