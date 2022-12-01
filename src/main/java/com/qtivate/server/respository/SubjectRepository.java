package com.qtivate.server.respository;

import com.mongodb.client.result.DeleteResult;
import com.qtivate.server.model.Class;
import com.qtivate.server.model.StudentPresence;
import com.qtivate.server.model.Subject;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface SubjectRepository extends MongoRepository<Subject, String> {
    @Query("{'meetings.classes.classId':'?0'}")
    Subject findSubjectByClassId(String classId);

    @Query("{'subId':'?0'}")
    Subject findSubjectBySubId(String subId);

    @Aggregation(pipeline = {
            "{'$match': {'meetings.classes.classId': ?0}}",
            "{'$unwind': {path: '$meetings'}}",
            "{'$unwind': {path: '$meetings.classes'}}",
            "{'$match': {'meetings.classes.classId': ?0}}",
            "{'$project':" +
                    "{_id: 0," +
                    "'class': '$meetings.classes.classId'," +
                    "start: '$meetings.classes.start'," +
                    "end: '$meetings.classes.end'," +
                    "tokens: '$meetings.classes.tokens'," +
                    "present: '$meetings.classes.present'}}"
    })
    Class findSubjectWithSpecificClassById(String classId);

    @Aggregation(pipeline = {
            "{'$project': {subId: 1, meetings: 1, meetings2: '$meetings'}}",
            "{'$unwind': {path: '$meetings2'}}",
            "{'$unwind': {path: '$meetings2.classes'}}",
            "{'$group':" +
                    "{_id: '$subId'," +
                    "meetings: {$first: '$meetings'}," +
                    "totalClasses: {$sum: 1}}}",
            "{'$unwind': {path: '$meetings'}}",
            "{'$unwind': {path: '$meetings.classes'}}",
            "{'$unwind': {path: '$meetings.classes.present'}}",
            "{'$group':" +
                    "{_id: {student: '$meetings.classes.present', subject: '$_id'}," +
                    "count: {$sum: 1}," +
                    "total: {$first: '$totalClasses'}}}",
            "{'$project':" +
                    "{_id: 0," +
                    "aid: '$_id.student'," +
                    "subId: '$_id.subject'," +
                    "count: '$count'," +
                    "total: '$total'," +
                    "presence: {$divide: ['$count', '$total']}}}",
            "{'$match': {aid: ?0}}",
    })
    List<StudentPresence> getStudentPresenceByAid(String aid);


    @Aggregation(pipeline = {
           "{'$match': {'meetings.classes.classId': ?0}}",
            "{'$unwind': {path: '$meetings'}}",
            "{'$project': {" +
                    "  _id: 0," +
                    "  'classes': {$filter: {" +
                    "                input: '$meetings.classes'," +
                    "                as: \"item\"," +
                    "                cond: {$eq: [\"$$item.classId\", \"12345P-SFW-22-2-001\"]}\n" +
                    "}}}}",
            "{'$match':  {\"classes.0\": {" +
                    "                \"$exists\": true" +
                    "            }}}",
            "{'$project':  {" +
                    "  _id: 0," +
                    "  'presents': '$classes.present'" +
                    "}}"
    })
    String getPresencesByClassId(String classId);


}
