package com.qtivate.server.service;

import com.qtivate.server.model.Student;
import com.qtivate.server.respository.StudentRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

public class StudentServiceTest {
    @InjectMocks
    private StudentService studentService;

    @Mock
    private StudentRepository studentRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldAddStudent() {
        Student newStudent = buildStudent();
        int result = studentService.addStudent(newStudent);
        assertEquals(result, 0);
    }

    @Test
    public void getAllStudents() {
        Student a = buildStudent("aidA", "nameA");
        Student b = buildStudent("aidB", "nameB");
        List<Student> list = List.of(a, b);
        doReturn(list).when(studentRepository).findAll();
        List<Student> result = studentService.getAllStudents();
        assertTrue(result.containsAll(list));
    }

    @Test
    public void getStudentByIdDoesntExist() {
        Student t = studentService.getStudentById("lorem");
        assertNull(t);
    }

    @Test
    public void getStudentsByIdReturnsRightStudent() {
        Student student = buildStudent();
        doReturn(student).when(studentRepository).findStudentById(student.getId());
        assertEquals(studentService.getStudentById(student.getId()), student);
    }

    @Test
    public void getStudentByAid() {
        Student student = buildStudent();
        doReturn(student).when(studentRepository).findStudentByAID(student.getAID());
        assertEquals(studentService.getStudentByAID(student.getAID()), student);
    }

    @Test
    public void updateStudentByIdDoesntExist() {
        Student student = buildStudent("aid", "name");
        Student newStudent = buildStudent("aid2", "name2");
        doReturn(null).when(studentRepository).findStudentById(student.getId());
        int result = studentService.updateStudentById(student.getId(), newStudent);
        assertEquals(result, 2);
    }

    @Test
    public void updateStudentByIdExists() {
        Student student = buildStudent("aid", "name");
        Student newStudent = buildStudent("aid2", "name2");
        doReturn(student).when(studentRepository).findStudentById(student.getId());
        int result = studentService.updateStudentById(student.getId(), newStudent);
        assertEquals(result, 0);
        Student updated = studentRepository.findStudentById(student.getId());
        assertEquals(updated.getAID(), newStudent.getAID());
        assertEquals(updated.getName(), newStudent.getName());
    }
    @Test
    public void deleteStudentById() {
        Student toBeDeleted = buildStudent();
        studentRepository.deleteById(toBeDeleted.getId());
        Student found = studentRepository.findStudentById(toBeDeleted.getId());
        assertNull(found);
    }

    private Student buildStudent() {
        Student student = new Student("aid", "name");
        student.setId(String.valueOf(student.hashCode()));
        return student;
    }

    private Student buildStudent(String aid, String name) {
        Student student = new Student(aid, name);
        student.setId(String.valueOf(student.hashCode()));
        return student;
    }
}
