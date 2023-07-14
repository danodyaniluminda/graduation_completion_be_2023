package com.ousl.graduation_completion.service.impl;

import com.ousl.graduation_completion.service.OptionalCourseCreditCheckerService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class OptionalCourseCreditCheckerServiceImpl implements OptionalCourseCreditCheckerService {

    @PersistenceContext
    EntityManager em;

    Logger logger = LoggerFactory.getLogger(OptionalCourseCreditCheckerServiceImpl.class);

    @Override
    public List<Object[]> checkCoursesNeedToBeConverted(Integer programId) {
        try {
            String sql = "select distinct course_id, application_id " +
                    "from student " +
                    "where program_id = :programmeId and valid=:valid and (subject_id=null or subject_id=1) and level=:level and course_type = :courseType and course_id not in (select program_course.course_id from program_course where program_course.program_id= :programmeId)";
            Query query = em.createNativeQuery(sql);
            query.setParameter("programmeId",programId);
            query.setParameter("level",3);
            query.setParameter("courseType",5);
            query.setParameter("valid",false);


            List<Object[]> results = query.getResultList();

            if(!results.isEmpty()){
                logger.info("some courses are not valid");
                return results;
            }
            else{
                String updateSql = "update student set valid=true where  course_type=:courseType and (subject_id=null or subject_id=1) and level=:level and  program_id = :programmeId and  valid=false and course_id in (select program_course.course_id from program_course where program_course.program_id= :programmeId)";
                Query updatequery = em.createNativeQuery(updateSql);
                query.setParameter("programmeId",programId);
                query.setParameter("level",3);
                query.setParameter("courseType",5);
                updatequery.executeUpdate();
            }

        } catch (Exception e) {
            logger.info("Error! "+e);

        }
        return null;
    }

    @Override
    public HashMap<String, Object> checkRegularCourseCredits(Long applicationId, Long programId, Integer level, Integer noOfCreditsRequired) {
        return null;
    }

}
