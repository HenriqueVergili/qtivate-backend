package com.qtivate.server.service;

import com.qtivate.server.model.Student;
import com.qtivate.server.respository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    // CREATE
    public int addStudent(Student student) {
        try {
            studentRepository.insert(student);
        } catch (Exception error) {
            System.err.println(error.getMessage());
            return 1;
        }
        return 0;
    }

    // READ
    // Get all
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Get by name
    public Student getStudentByName(String name) {
        return studentRepository.findStudentByName(name);
    }

    // Get by id
    public Student getStudentById(String id) {
        return studentRepository.findStudentById(id);
    }

    // Get by Academic ID
    public Student getStudentByAID(String aid) {
        return studentRepository.findStudentByAID(aid);
    }

    // Get count of documents
    public long getCountOfDocuments() {
        return studentRepository.count();
    }

    // UPDATE
    public int updateStudentById(String id, Student newStudent) {
        try {
            Student student = studentRepository.findStudentById(id);
            if (student == null) return 2;
            student.changeAll(newStudent);
            studentRepository.save(student);
            return 0;
        } catch (Exception error) {
            System.err.println(error.getMessage());
            return 1;
        }
    }

    // DELETE
    public int deleteStudentById(String id) {
        try {
            if (studentRepository.findStudentById(id) == null) return 2;
            studentRepository.deleteById(id);
            return 0;
        } catch (Exception error) {
            System.err.println(error.getMessage());
            return 1;
        }
    }

}
