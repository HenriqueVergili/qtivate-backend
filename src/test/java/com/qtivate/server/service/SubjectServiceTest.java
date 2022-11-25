package com.qtivate.server.service;

import com.qtivate.server.model.Class;
import com.qtivate.server.model.Meeting;
import com.qtivate.server.model.Student;
import com.qtivate.server.model.Subject;
import com.qtivate.server.respository.SubjectRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

public class SubjectServiceTest {
    @InjectMocks
    private SubjectService subjectService;

    @Mock
    private SubjectRepository subjectRepository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldAddInPresenceByClassId() {
        String classId = "123";
        String studentAid = "345";
        when(subjectRepository.findSubjectByClassId(classId)).thenReturn(buildSubjectByClassIdAndAid(classId, "567", studentAid));
        assertEquals(subjectService.addInPresenceByClassId(classId, "567"), 0);
    }

    @Test
    public void setPresenceByClassIdAndListOfAids() {
        String classId = "123";
        String studentAid = "345";
        Subject oldSubject = buildSubjectByClassIdAndAid(classId, studentAid, studentAid);
        when(subjectRepository.findSubjectByClassId(classId)).thenReturn(oldSubject);
        List<String> setForAids = List.of("456", "789");
        Map<String, String> result = subjectService.setPresenceByClassId(classId, setForAids);
        assertEquals(2, result.get("added").split(",").length);
        assertTrue(Arrays.asList("456", "789").containsAll(List.of(result.get("added").split(","))));
        assertTrue(Arrays.asList("345", "anotherAid").containsAll(List.of(result.get("removed").split(","))));
    }

    @Test
    public void addTokens() {
        String classId = "123";
        String[] tokens = {"456", "789"};
        Subject subject = buildSubjectByClassIdAndAid(classId, "aid", "aid");
        doReturn(subject).when(subjectRepository).findSubjectByClassId(classId);
        assertEquals(0, subjectService.addTokensByClassId(classId, tokens));

    }

    private Subject buildSubjectByClassIdAndAid(String classId, String aid, String aidPresent) {
        Subject newSubject = new Subject();
        newSubject.setStudents(Arrays.asList(new Student(aid, "name")));
        Class newClass = new Class();
        ArrayList arrayList = new ArrayList();
        arrayList.add(aidPresent);
        arrayList.add("anotherAid");
        newClass.setPresent(arrayList);
        newClass.setClassId(classId);
        Meeting newMeeting = new Meeting(new Date(), Arrays.asList(newClass));
        newSubject.setMeetings(Arrays.asList(newMeeting));
        return newSubject;
    }
}
