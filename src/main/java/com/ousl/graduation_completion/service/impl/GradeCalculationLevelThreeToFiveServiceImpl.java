package com.ousl.graduation_completion.service.impl;
import com.ousl.graduation_completion.models.*;
import com.ousl.graduation_completion.repository.*;
import com.ousl.graduation_completion.service.GradeCalculationLevelThreeToFiveService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class GradeCalculationLevelThreeToFiveServiceImpl implements GradeCalculationLevelThreeToFiveService {
    @PersistenceContext
    EntityManager em;
    Logger logger = LoggerFactory.getLogger(GenerateResultServiceImpl.class);

    @Autowired
    RuleLogRepository ruleLogRepository;

    @Autowired
    private ProgramCriterionRepository programCriterionRepository;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private CriterionRepository criterionRepository;

    @Autowired
    private StudentFailedCriteriaDetailRepository studentFailedCriteriaDetailRepository;

    @Override
    @Transactional

    //=============================================================================================================================================================================

    public HashMap<String, Object> gradeCalculationLevelThree(Long programId) {
        HashMap<String, Object> response = new HashMap<>();
        try {

            Optional<ProgramCriterion> programCriterion = programCriterionRepository.getProgramCriterionByProgram_IdAndActiveAndCriteria_Id(programId, true, 5L);

            if (programCriterion.isPresent()) {
                Program program = programRepository.findById(programId).orElse(null);
                deleteAllStudentFailedCriteria(program,5L);


            /*
            Insert data to student_failed_criteria_detail table,
            This quary for S1 or NS2 sylabus
            if student have level 3 grade validation in 164 -  Bachelor of Science program
             */
                String LevelThreeGrade =
                        "INSERT INTO student_failed_criteria_detail (program_id, student_id, criteria_id, status, details,created_by) \n" +
                                "SELECT program_id, application_id, 5,\n" +
                                "       CASE\n" +
                                "           WHEN application_id IN (SELECT DISTINCT s.application_id\n" +
                                "                                   FROM student s\n" +
                                "                                   WHERE s.valid = true\n" +
                                "                                     AND s.level = :level\n" +
                                "                                     AND s.grade_map_id > 5\n" +
                                "                                     AND course_type <> 3\n" +
                                "                                     AND s.program_id = :programId\n" +
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
                                "               THEN 'Successfully Completed Level 3 Grade Validation'\n" +
                                "           ELSE 'Not Completed Level 3 Grade Validation'\n" +
                                "           END,\n" +
                                "       1\n" +
                                "FROM student s\n" +
                                "WHERE s.program_id = :programId AND s.level = :level\n" +
                                "GROUP BY s.application_id, s.program_id";
                Query query3 = em.createNativeQuery(LevelThreeGrade);
                query3.setParameter("programId", programId);
                query3.setParameter("level", 3);
                int upCount = query3.executeUpdate();
                logger.info("Success ! Level 3 grade validated. " + upCount + " number of rows were updated");
                response.put("message", "success");
                response.put("rows were Updated", upCount);

                List<StudentFailedCriteriaDetail> getInsertedStudents = studentFailedCriteriaDetailRepository.getAllByCriteria_IdAndStatusAndProgram(5L, "pass", program);
                updateRuleLog(5L, 3, getInsertedStudents);

                getInsertedStudents = studentFailedCriteriaDetailRepository.getAllByCriteria_IdAndStatusAndProgram(5L, "fail", program);
                updateRuleLog(5L, 3, getInsertedStudents);

                program = programRepository.findById(programId).orElse(null);
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

    //=============================================================================================================================================================================
    @Override
    @Transactional

    public HashMap<String, Object> gradeCalculationLevelFour(Long programId) {
        HashMap<String, Object> response = new HashMap<>();
        try {

            Optional<ProgramCriterion> programCriterion = programCriterionRepository.getProgramCriterionByProgram_IdAndActiveAndCriteria_Id(programId, true, 7L);

            if (programCriterion.isPresent()) {
                Program program = programRepository.findById(programId).orElse(null);
                deleteAllStudentFailedCriteria(program,7L);


            /*
            Insert data to student_failed_criteria_detail table,
            This quary for S1 or NS2 sylabus
            if student have level 4 validation in 164 -  Bachelor of Science program
             */
                String LevelFourGrade =
                        "INSERT INTO student_failed_criteria_detail (program_id, student_id, criteria_id, status, details, create_date,created_by)\n" +
                                "SELECT program_id,\n" +
                                "       application_id,\n" +
                                "       7,\n" +
                                "       CASE\n" +
                                "           WHEN application_id IN (SELECT DISTINCT s.application_id\n" +
                                "                                   FROM student s\n" +
                                "                                   WHERE s.valid = true\n" +
                                "                                     AND s.level = :level\n" +
                                "                                     AND s.grade_map_id > 5\n" +
                                "                                     AND course_type <> 3\n" +
                                "                                     AND s.program_id = :programId\n" +
                                "                                   GROUP BY s.application_id\n" +
                                "                                   HAVING SUM(s.credit) >= 27)\n" +
                                "\n" +
                                "                            THEN CASE\n" +
                                "                                     WHEN application_id IN (SELECT DISTINCT s.application_id\n" +
                                "                                                             FROM student s\n" +
                                "                                                             WHERE s.valid = true\n" +
                                "                                                               AND s.level = :level\n" +
                                "                                                               AND s.grade_map_id > 2\n" +
                                "                                                               AND course_type <> 3\n" +
                                "                                                               AND s.program_id = :programId\n" +
                                "                                                             GROUP BY s.application_id\n" +
                                "                                                             HAVING SUM(s.credit) >= 30)\n" +
                                "                                         THEN 'pass'\n" +
                                "           ELSE 'fail'\n" +
                                "               END\n" +
                                "           ELSE 'fail'\n" +
                                "           END,\n" +
                                "       CASE\n" +
                                "           WHEN application_id IN (SELECT DISTINCT s.application_id\n" +
                                "                                   FROM student s\n" +
                                "                                   WHERE s.valid = true\n" +
                                "                                     AND s.level = :level\n" +
                                "                                     AND s.grade_map_id > 5\n" +
                                "                                     AND course_type <> 3\n" +
                                "                                     AND s.program_id = :programId\n" +
                                "                                   GROUP BY s.application_id\n" +
                                "                                   HAVING SUM(s.credit) >= 27)\n" +
                                "\n" +
                                "                            THEN CASE\n" +
                                "                                     WHEN application_id IN (SELECT DISTINCT s.application_id\n" +
                                "                                                             FROM student s\n" +
                                "                                                             WHERE s.valid = true\n" +
                                "                                                               AND s.level = :level\n" +
                                "                                                               AND s.grade_map_id > 2\n" +
                                "                                                               AND course_type <> 3\n" +
                                "                                                               AND s.program_id = :programId\n" +
                                "                                                             GROUP BY s.application_id\n" +
                                "                                                             HAVING SUM(s.credit) >= 30)\n" +
                                "                                         THEN 'Successfully Completed Level 4 Grade Validation'\n" +
                                "                                   ELSE 'Not Completed Level 4 Grade Validation'\n" +
                                "               END\n" +
                                "           ELSE 'Not Completed Level 4 Grade Validation'\n" +
                                "           END,\n" +
                                "       CURRENT_TIMESTAMP,\n" +
                                "       1\n" +
                                "FROM student\n" +
                                "WHERE program_id = :programId\n" +
                                "  AND level = :level\n" +
                                "\n" +
                                "GROUP BY application_id, program_id";
                Query query4 = em.createNativeQuery(LevelFourGrade);
                query4.setParameter("programId", programId);
                query4.setParameter("level", 4);
                int upCount1 = query4.executeUpdate();
                logger.info("Success ! Success ! Level 4 grade validated. " + upCount1 + " number of rows were updated");
                response.put("message", "success");
                response.put("rows were Updated", upCount1);

                List<StudentFailedCriteriaDetail> getInsertedStudents = studentFailedCriteriaDetailRepository.getAllByCriteria_IdAndStatusAndProgram(7L, "pass", program);
                updateRuleLog(7L, 4, getInsertedStudents);

                getInsertedStudents = studentFailedCriteriaDetailRepository.getAllByCriteria_IdAndStatusAndProgram(7L, "fail", program);
                updateRuleLog(7L, 4, getInsertedStudents);

                program = programRepository.findById(programId).orElse(null);
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

//=============================================================================================================================================================================

    @Override
    @Transactional

    public HashMap<String, Object> gradeCalculationLevelFive(Long programId) {
        HashMap<String, Object> response = new HashMap<>();
        try {

            Optional<ProgramCriterion> programCriterion = programCriterionRepository.getProgramCriterionByProgram_IdAndActiveAndCriteria_Id(programId, true, 8L);

            if (programCriterion.isPresent()) {
                Program program = programRepository.findById(programId).orElse(null);
                deleteAllStudentFailedCriteria(program,8L);


            /*
            Insert data to student_failed_criteria_detail table,
            This quary for S1 or NS2 sylabus
            if student have level 5 validation in 164 -  Bachelor of Science program
             */
                String LevelFourGrade =
                        "INSERT INTO student_failed_criteria_detail (program_id, student_id, criteria_id, status, details, create_date,created_by)\n" +
                                "SELECT program_id,\n" +
                                "       application_id,\n" +
                                "       8,\n" +
                                "       CASE\n" +
                                "           WHEN application_id IN (SELECT DISTINCT s.application_id\n" +
                                "                                   FROM student s\n" +
                                "                                   WHERE s.valid = true\n" +
                                "                                     AND s.level = :level\n" +
                                "                                     AND s.grade_map_id > 5\n" +
                                "                                     AND course_type <> 3\n" +
                                "                                     AND s.program_id = :programId\n" +
                                "                                   GROUP BY s.application_id\n" +
                                "                                   HAVING SUM(s.credit) >= 24)\n" +
                                "\n" +
                                "                            THEN CASE\n" +
                                "                                     WHEN application_id IN (SELECT DISTINCT s.application_id\n" +
                                "                                                             FROM student s\n" +
                                "                                                             WHERE s.valid = true\n" +
                                "                                                               AND s.level = :level\n" +
                                "                                                               AND s.grade_map_id > 2\n" +
                                "                                                               AND course_type <> 3\n" +
                                "                                                               AND s.program_id = :programId\n" +
                                "                                                             GROUP BY s.application_id\n" +
                                "                                                             HAVING SUM(s.credit) >= 30)\n" +
                                "                                         THEN 'pass'\n" +
                                "           ELSE 'fail'\n" +
                                "               END\n" +
                                "           ELSE 'fail'\n" +
                                "           END,\n" +
                                "       CASE\n" +
                                "           WHEN application_id IN (SELECT DISTINCT s.application_id\n" +
                                "                                   FROM student s\n" +
                                "                                   WHERE s.valid = true\n" +
                                "                                     AND s.level = :level\n" +
                                "                                     AND s.grade_map_id > 5\n" +
                                "                                     AND course_type <> 3\n" +
                                "                                     AND s.program_id = :programId\n" +
                                "                                   GROUP BY s.application_id\n" +
                                "                                   HAVING SUM(s.credit) >= 24)\n" +
                                "\n" +
                                "                            THEN CASE\n" +
                                "                                     WHEN application_id IN (SELECT DISTINCT s.application_id\n" +
                                "                                                             FROM student s\n" +
                                "                                                             WHERE s.valid = true\n" +
                                "                                                               AND s.level = :level\n" +
                                "                                                               AND s.grade_map_id > 2\n" +
                                "                                                               AND course_type <> 3\n" +
                                "                                                               AND s.program_id = :programId\n" +
                                "                                                             GROUP BY s.application_id\n" +
                                "                                                             HAVING SUM(s.credit) >= 30)\n" +
                                "                                         THEN 'Successfully Completed Level 5 Grade Validation'\n" +
                                "                           ELSE 'Not Completed Level 5 Grade Validation'\n" +
                                "               END\n" +
                                "           ELSE 'Not Completed Level 5 Grade Validation'\n" +
                                "           END,\n" +
                                "       CURRENT_TIMESTAMP,\n" +
                                "       1\n" +
                                "FROM student\n" +
                                "WHERE program_id = :programId\n" +
                                "  AND level = :level\n" +
                                "\n" +
                                "GROUP BY application_id, program_id";
                Query query5 = em.createNativeQuery(LevelFourGrade);
                query5.setParameter("programId", programId);
                query5.setParameter("level", 5);
                int upCount2 = query5.executeUpdate();
                logger.info("Success ! Success ! Level 5 grade validated. " + upCount2 + " number of rows were updated");
                response.put("message", "success");
                response.put("rows were Updated", upCount2);

                List<StudentFailedCriteriaDetail> getInsertedStudents = studentFailedCriteriaDetailRepository.getAllByCriteria_IdAndStatusAndProgram(8L, "pass", program);
                updateRuleLog(8L, 5, getInsertedStudents);

                getInsertedStudents = studentFailedCriteriaDetailRepository.getAllByCriteria_IdAndStatusAndProgram(8L, "fail", program);
                updateRuleLog(8L, 5, getInsertedStudents);

                program = programRepository.findById(programId).orElse(null);
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

//=============================================================================================================================================================================

    //Update Rule log Method
    private void updateRuleLog(Long criteriaId, int level, Iterable<? extends StudentFailedCriteriaDetail> getInsertedStudents) {
        try {
            Criterion criterion = criterionRepository.findById(criteriaId).get();

            for (StudentFailedCriteriaDetail student : getInsertedStudents) {
                RuleLog ruleLog = new RuleLog();
                ruleLog.setStudent(student.getStudent());
                ruleLog.setProgram(student.getProgram());
                ruleLog.setCreateDate(LocalDateTime.now());
                ruleLog.setLog("Level " + level + " " + criterion.getCriteriaName() + " checked");
                ruleLogRepository.save(ruleLog);
            }
            logger.info("Success ! updated Rule Log");


        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Error ! update Rule Log " + e.getMessage());
        }
    }


    //Update program sequence
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

    private void deleteAllStudentFailedCriteria(Program program, Long criteriaId) {
        Criterion criterion = criterionRepository.findById(criteriaId).get();
        studentFailedCriteriaDetailRepository.deleteAllByCriteriaAndProgram(criterion, program);
    }
}

