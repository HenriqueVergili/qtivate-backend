package com.qtivate.server.respository;

import com.qtivate.server.model.Student;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface StudentRepository extends MongoRepository<Student, String> {

    @Query("{name:'?0'}")
    Student findStudentByName(String name);

    @Query("{_id: '?0'}")
    Student findStudentById(String id);

    @Query("{aid: '?0'}")
    Student findStudentByAID(String aid);

    @Query(value="{aid:'?0'}", fields="{'name':  1, 'aid':  1}")
    List<Student> findAll(String aid);

    public long count();
}
