package com.ousl.graduation_completion.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.ousl.graduation_completion.service.RegularCourseCreditCheckerService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transaction;
import jakarta.transaction.Transactional;
import jakarta.websocket.Session;
import org.hibernate.SessionFactory;
import org.hibernate.SharedSessionContract;
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
    @Transactional
    public HashMap<String, Object> checkRegularCourseCredits(Long programId, Integer level, Integer noOfCreditsRequired) {

        HashMap<String, Object> response = new HashMap<>();

        try{

            if(level==3){

                Query level3CreditRegularCheckerQuery = em.createNativeQuery(
                        "select level_3_regular_credit_checker(:programId,:noOfCreditsRequired,:level)",String.class
                );
                level3CreditRegularCheckerQuery.setParameter("programId", programId);
                level3CreditRegularCheckerQuery.setParameter("level", level);
                level3CreditRegularCheckerQuery.setParameter("noOfCreditsRequired", noOfCreditsRequired);
                String jsonText = (String) level3CreditRegularCheckerQuery.getSingleResult();
                JsonObject result = new Gson().fromJson(jsonText, JsonObject.class);
                response.put("result", result);
                //https://stackoverflow.com/questions/61169128/could-not-write-json-jsonobject-nested-exception-is-com-fasterxml-jackson-data
            }

            response.put("status", true);
            response.put("message", "success");
            return response;

        }catch (Exception e){
            System.out.println(e.getLocalizedMessage());
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            System.out.println(e.getClass());
            response.put("status", false);
            response.put("message", "error : " + e.getLocalizedMessage());
            return response;
        }
    }
}
