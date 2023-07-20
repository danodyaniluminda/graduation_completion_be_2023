package com.ousl.graduation_completion.service.impl;

import com.ousl.graduation_completion.models.Program;
import com.ousl.graduation_completion.repository.ProgramRepository;
import com.ousl.graduation_completion.service.GradeCalculationLevelThreeToFiveService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class GradeCalculationLevelThreeToFiveServiceImpl implements GradeCalculationLevelThreeToFiveService {
    @PersistenceContext
    EntityManager em;

    Logger logger = LoggerFactory.getLogger(OptionalCourseCreditCheckerServiceImpl.class);

    @Autowired
    private ProgramRepository programRepository;

    @Override
    @Transactional
    public HashMap<String, Object> gradeCalculationLevelThree(Long programId) {
        HashMap<String, Object> response = new HashMap<>();
        try {
            /*
            Insert data to student_failed_criteria_detail table,
            This quary for S1 or NS2 sylabus
            if student have (level 3 course 30 credits) in 164 -  Bachelor of Science program
            He/She will pass Level 3.
             */
            String LevelThreeGrade =
                    "INSERT INTO student_failed_criteria_detail (program_id, student_id, criteria_id, status, details,created_by) \n" +
                    "SELECT program_id, application_id, 5,\n" +
                    "       CASE\n" +
                    "           WHEN application_id IN (SELECT DISTINCT s.application_id\n" +
                    "                                   FROM student s\n" +
                    "                                   WHERE s.valid = true\n" +
                    "                                     AND s.level = 3\n" +
                    "                                     AND s.grade_map_id > 5\n" +
                    "                                     AND course_type <> 3\n" +
                    "                                     AND s.program_id = 164\n" +
                    "                                   GROUP BY s.application_id\n" +
                    "                                   HAVING SUM(s.credit) >= 30)\n" +
                    "               THEN 'pass'\n" +
                    "           ELSE 'fail'\n" +
                    "           END,\n" +

                    "              CASE\n" +
                    "           WHEN application_id IN (SELECT DISTINCT s.application_id\n" +
                    "                                   FROM student s\n" +
                    "                                   WHERE s.valid = true\n" +
                    "                                     AND s.level = :level\n" +
                    "                                     AND s.grade_map_id > 5\n" +
                    "                                     AND course_type <> 3\n" +
                    "                                     AND s.program_id = :programId\n" +
                    "                                   GROUP BY s.application_id\n" +
                    "                                   HAVING SUM(s.credit) >= 30)\n" +
                    "               THEN 'Have Level 3, 30 C passes'\n" +
                    "           ELSE 'No Level 3 simple passes for 30 credits'\n" +
                    "           END,\n" +
                    "       1\n" +
                    "FROM student s\n" +
                    "WHERE s.program_id = :programId AND s.level = :level\n" +
                    "GROUP BY s.application_id, s.program_id";

            Query query = em.createNativeQuery(LevelThreeGrade);
            query.setParameter("programId", programId);
            query.setParameter("level", 3);
            int upCount = query.executeUpdate();
            logger.info("Success ! Level 3 30 C passes credit count was checked. " + upCount + " number of rows were updated");

            response.put("message", "success");
            response.put("rowsUpdated", upCount);
//            response.put("failCount", failCount);

//            Program program = programRepository.findById(programId).orElse(null);

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error! " + e.getMessage());

        }

        return null;
    }
}
