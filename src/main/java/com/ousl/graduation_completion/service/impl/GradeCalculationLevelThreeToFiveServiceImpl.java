package com.ousl.graduation_completion.service.impl;
import com.ousl.graduation_completion.models.Program;
import com.ousl.graduation_completion.models.ProgramCriterion;
import com.ousl.graduation_completion.models.RuleLog;
import com.ousl.graduation_completion.models.Student;
import com.ousl.graduation_completion.repository.ProgramCriterionRepository;
import com.ousl.graduation_completion.repository.ProgramRepository;
import com.ousl.graduation_completion.repository.RuleLogRepository;
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
import java.util.List;
import java.util.Optional;

@Service
public class GradeCalculationLevelThreeToFiveServiceImpl implements GradeCalculationLevelThreeToFiveService {
    @PersistenceContext
    EntityManager em;
    Logger logger = LoggerFactory.getLogger(OptionalCourseCreditCheckerServiceImpl.class);

    @Autowired
    RuleLogRepository ruleLogRepository;

    @Autowired
    private ProgramCriterionRepository programCriterionRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Override
    @Transactional
    public HashMap<String, Object> gradeCalculationLevelThree(Long programId) {
        HashMap<String, Object> response = new HashMap<>();
        try {

            Optional<ProgramCriterion> programCriterion = programCriterionRepository.getProgramCriterionByProgram_IdAndActiveAndCriteria_Id(programId, true, 3L);

            if (programCriterion.isPresent()) {


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
                response.put("rows were Updated", upCount);


                Program program = programRepository.findById(programId).orElse(null);
                updateProgramSequence(program);

                return response;

            }
            response.put("message", "Already updated.");
            return response;

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("Error! " + e.getMessage());
            return response;
        }
    }


    private void updateRuleLog(Student student, Program program) {
        try {
            RuleLog ruleLog = new RuleLog();
            ruleLog.setStudent(student);
            ruleLog.setProgram(program);
            ruleLog.setLog("");
            ruleLogRepository.save(ruleLog);
            logger.info("Success ! updated Rule Log");

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Error ! update Rule Log " + e.getMessage());
        }
    }

    private void updateProgramSequence(Program program) {
        try {
            List<ProgramCriterion> programCriterionList = programCriterionRepository.getAllByProgramAndActive(program, true);
            ProgramCriterion programCriterion = programCriterionRepository.getProgramCriterionByProgramAndActive(program, true);

            for (ProgramCriterion p : programCriterionList) {
                p.setActive(false);
                programCriterionRepository.save(p);
            }

            ProgramCriterion newProgramCriterion = programCriterionRepository.getProgramCriterionBySequenceIdAndProgram(programCriterion.getSequenceId() + 1, program);
            if (newProgramCriterion != null) {
                newProgramCriterion.setActive(true);
                programCriterionRepository.save(newProgramCriterion);
            }
            logger.info("Success ! Update Program Sequence");


        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Error ! Update Program Sequence " + e.getMessage());
        }
    }
}