package com.ousl.graduation_completion.service.impl;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.ousl.graduation_completion.service.RegularCourseCreditCheckerService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
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

            String sqlQuery = "select level_" + level.toString() +"_regular_credit_checker(:programId,:noOfCreditsRequired,:level)";


            Query regularCreditCheckerQuery = em.createNativeQuery(sqlQuery, String.class);
            regularCreditCheckerQuery.setParameter("programId", programId);
            regularCreditCheckerQuery.setParameter("level", level);
            regularCreditCheckerQuery.setParameter("noOfCreditsRequired", noOfCreditsRequired);
            String jsonText = (String) regularCreditCheckerQuery.getSingleResult();
            JsonObject result = new Gson().fromJson(jsonText, JsonObject.class);
            response.put("total", result.get("total"));
            response.put("totalPass", result.get("totalPass"));
            response.put("message", result.get("message"));
            response.put("totalPassConflict", result.get("totalPassConflict"));
            response.put("totalFail", result.get("totalFail"));
            response.put("conflict", result.get("conflict"));
            response.put("conflictExcel", result.get("conflictExcel"));
            //https://stackoverflow.com/questions/61169128/could-not-write-json-jsonobject-nested-exception-is-com-fasterxml-jackson-data

            response.put("status", "success");
            return response;

        }catch (Exception e){
            System.out.println(e.getLocalizedMessage());
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            System.out.println(e.getClass());

            response.put("status", "error : " + e.getLocalizedMessage());
            return response;
        }
    }

    @Override
    @Transactional
    public HashMap<String, Object> checkS1RegularCourseCredits(Long programId,Integer level) {

        HashMap<String, Object> response = new HashMap<>();

        try{

            String sqlQuery = "select s1_level_" + level.toString() +"_regular_credit_checker(:programId)";


            Query regularCreditCheckerQuery = em.createNativeQuery(sqlQuery, String.class);
            regularCreditCheckerQuery.setParameter("programId", programId);
            String jsonText = (String) regularCreditCheckerQuery.getSingleResult();
            JsonObject result = new Gson().fromJson(jsonText, JsonObject.class);
            response.put("total", result.get("total"));
            response.put("totalPass", result.get("totalPass"));
            response.put("message", result.get("message"));
            response.put("totalPassConflict", result.get("totalPassConflict"));
            response.put("totalFail", result.get("totalFail"));
            response.put("conflict", result.get("conflict"));
            response.put("conflictExcel", result.get("conflictExcel"));
            //https://stackoverflow.com/questions/61169128/could-not-write-json-jsonobject-nested-exception-is-com-fasterxml-jackson-data

            response.put("status", "success");
            return response;

        }catch (Exception e){
            System.out.println(e.getLocalizedMessage());
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            System.out.println(e.getClass());

            response.put("status", "error : " + e.getLocalizedMessage());
            return response;
        }
    }
}
