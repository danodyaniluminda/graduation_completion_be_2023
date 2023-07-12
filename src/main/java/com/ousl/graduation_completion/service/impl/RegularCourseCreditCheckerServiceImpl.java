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
    public HashMap<String, Object> checkRegularCourseCredits(Long applicationId, Long programId,Integer level, Integer noOfCreditsRequired) {

        HashMap<String, Object> response = new HashMap<>();
        Long noOfCreditsObtained = -1L;

        try{
            Query query = em.createNativeQuery(
                    "SELECT SUM(s.credit) from student AS s\n" +
                            "WHERE s.application_id=:applicationId AND s.course_type=:courseType AND s.program_id=:programId AND s.level=:level;", Long.class);
            query.setParameter("applicationId", applicationId);
            query.setParameter("courseType", 1);
            query.setParameter("programId", programId);
            query.setParameter("level", level);
            noOfCreditsObtained = (Long) query.getSingleResult();
            Boolean pass = noOfCreditsObtained >= noOfCreditsRequired;
            response.put("applicationId", applicationId);
            response.put("level", level);
            response.put("pass", pass);
            response.put("noOfCreditsRequired", noOfCreditsRequired);
            response.put("noOfCreditsObtained", noOfCreditsObtained);
            response.put("status", true);
            response.put("message", "success");

            return response;

        }catch (Exception e){
            response.put("applicationId", applicationId);
            response.put("level", level);
            response.put("pass", false);
            response.put("noOfCreditsRequired", noOfCreditsRequired);
            response.put("noOfCreditsObtained", noOfCreditsObtained);
            response.put("status", false);
            response.put("message", "error : " + e.getLocalizedMessage());
            return response;
        }
    }

    @Override
    public List<String> checkCoursesNeedToBeConverted(Long programId) {
        try {
            Query query = em.createNativeQuery(
                    "select\n" +
                            "    json_build_object(\n" +
                            "        'courseId', json_agg(s.course_id),\n" +
                            "        'applicationId', json_agg(s.application_id)\n" +
                            "    )\n" +
                            "from student  as s\n" +
                            "where s.program_id=:programId and s.course_type=1 and s.course_id not in (select program_course.course_id from program_course where s.program_id=1 and course_type=1);"
                    ,String.class
            );
            query.setParameter("programId", programId);
            List<String> results = (List<String>)query.getResultList();
            return results;
        }catch (Exception e){
            return null;
        }
    }
}
