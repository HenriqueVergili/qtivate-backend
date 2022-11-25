package com.qtivate.server.api;

import com.qtivate.server.model.Student;
import com.qtivate.server.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("api/v1/students")
@RestController
public class StudentController {
    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<Student> addStudent(@RequestBody Student student) {
        studentService.addStudent(student);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(student);
    }

    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @RequestMapping(params = "id")
    public Student getStudentById(@RequestParam String id) {
        return studentService.getStudentById(id);
                //.orElse(null);
    }


    @RequestMapping(params = "aid")
    public Student getStudentByAID(@RequestParam String aid) {
        return studentService.getStudentByAID(aid);
        //.orElse(null);
    }

    @PutMapping(path = "{id}")
    public ResponseEntity<Student> updateStudentById(@PathVariable("id") String id, @RequestBody Student student) {
        int code = studentService.updateStudentById(id, student);
        if (code == 1) return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        if (code == 2) return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }

    @DeleteMapping(path = "{id}")
    public ResponseEntity<Student> deleteStudentById(@PathVariable("id") String id) {
        int code = studentService.deleteStudentById(id);
        if (code == 1) return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .build();
        if (code == 2) return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .build();
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .build();
    }
}
