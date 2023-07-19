package com.ousl.graduation_completion.service.impl;

import com.ousl.graduation_completion.service.RegularCourseCreditCheckerService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class RegularCourseCreditCheckerServiceImpl implements RegularCourseCreditCheckerService {

    @PersistenceContext
    EntityManager em;

    @Override
    public List<String> checkCoursesNeedToBeConverted(Long programId) {
        try {
            Query query = em.createNativeQuery(
                    "select\n" +
                            "    json_build_object(\n" +
                            "        'courseId', (s.course_id),\n" +
                            "        'applicationId', (s.application_id)\n" +
                            "    )" +
                            "from student  as s\n" +
                            "where s.program_id=:programId and s.course_type=2 and s.course_id not in (select program_course.course_id from program_course where s.program_id=1 and course_type=1);"
                    ,String.class
            );
            query.setParameter("programId", programId);
            List<String> results = (List<String>)query.getResultList();
            return results;
        }catch (Exception e){
            return null;
        }
    }

    @Override
    public HashMap<String, Object> checkRegularCourseCredits(Long programId, Integer level, Integer noOfCreditsRequired) {

        HashMap<String, Object> response = new HashMap<>();


        try{

            if(level==3){
                Query passQuery = em.createNativeQuery(
                        "select\n" +
                                "    json_build_object(\n" +
                                "        'applicationId', swecs. application_id,\n" +
                                "        'program_id', swecs.program_id,\n" +
                                "        'subjectIds', array_agg(swecs.subject_id),\n" +
                                "        'courseIds', array_agg(courseIds),\n" +
                                "        'totalCredits', sum(swecs.credit)\n" +
                                "        ) as row_json\n" +
                                "from\n" +
                                "(\n" +
                                "select s.application_id, ss.program_id, ss.subject_id, CONCAT('[',array_to_string(array_agg(s.course_id),','),']') as courseIds, sum(s.credit) as credit\n" +
                                "from student as s, student_subject ss\n" +
                                "where s.level=:level and\n" +
                                "      s.course_type=2 and\n" +
                                "      s.subject_id!=1 and\n" +
                                "      s.subject_id is not null and\n" +
                                "      s.program_id=:programId and\n" +
                                "      s.application_id = ss.student_id and\n" +
                                "      s.subject_id = ss.subject_id and\n" +
                                "      s.program_id = ss.program_id\n" +
                                "group by s.program_id, s.subject_id,ss.program_id, ss.subject_id, s.application_id\n" +
                                "having sum(credit)>=8\n" +
                                ") as swecs\n" +
                                "group by swecs.application_id, swecs.program_id\n" +
                                "having sum(swecs.credit)>=:noOfCreditsRequired;", String.class
                );
                passQuery.setParameter("programId", programId);
                passQuery.setParameter("level", level);
                passQuery.setParameter("noOfCreditsRequired", noOfCreditsRequired);
                List<String> passList = (List<String>)passQuery.getResultList();
                response.put("totalPass", passList.size());
                response.put("passList", passList);


                Query failQuery = em.createNativeQuery(
                        "select\n" +
                                "    json_build_object(\n" +
                                "        'applicationId', swnec. application_id,\n" +
                                "        'program_id', swnec.program_id,\n" +
                                "        'subjectIds', array_agg(swnec.subject_id),\n" +
                                "        'courseIds', array_agg(courseIds),\n" +
                                "        'totalCredits', sum(swnec.credit)\n" +
                                "        ) as row_json\n" +
                                "from\n" +
                                "(\n" +
                                "select s.application_id, ss.program_id, ss.subject_id, CONCAT('{',array_to_string(array_agg(s.course_id),','),'}') as courseIds, sum(s.credit) as credit\n" +
                                "from student as s, student_subject ss\n" +
                                "where  s.application_id = ss.student_id and\n" +
                                "       s.subject_id = ss.subject_id and\n" +
                                "       s.program_id = ss.program_id and\n" +
                                "       s.course_type=2 and s.level=3 and s.subject_id!=1 and s.subject_id is not null\n" +
                                "group by s.program_id, s.subject_id,ss.program_id, ss.subject_id, s.application_id\n" +
                                ") as swnec\n" +
                                "where swnec.application_id in\n" +
                                "(\n" +
                                "select s.application_id\n" +
                                "from student as s, student_subject ss\n" +
                                "where s.course_type=2 and\n" +
                                "      s.level=:level and\n" +
                                "      s.program_id=:programId and\n" +
                                "      s.subject_id!=1 and\n" +
                                "      s.subject_id is not null and\n" +
                                "      s.application_id = ss.student_id and\n" +
                                "      s.subject_id = ss.subject_id and\n" +
                                "      s.program_id = ss.program_id\n" +
                                "group by s.program_id, s.subject_id, s.application_id\n" +
                                "having sum(credit)<8\n" +
                                ")\n" +
                                "group by swnec.application_id, swnec.program_id;", String.class
                );
                failQuery.setParameter("programId", programId);
                failQuery.setParameter("level", level);
                //failQuery.setParameter("noOfCreditsRequired", noOfCreditsRequired);
                List<String> failList = (List<String>)failQuery.getResultList();
                response.put("totalFail", failList.size());
                response.put("failList", failList);
            }

            response.put("status", true);
            response.put("message", "success");
            return response;

        }catch (Exception e){

            response.put("status", false);
            response.put("message", "error : " + e.getLocalizedMessage());
            return response;
        }
    }
}
