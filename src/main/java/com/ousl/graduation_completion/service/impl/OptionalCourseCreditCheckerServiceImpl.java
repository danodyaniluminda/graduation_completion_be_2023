package com.ousl.graduation_completion.service.impl;

import com.ousl.graduation_completion.dto.response.OptionalCourseListResponeDto;
import com.ousl.graduation_completion.models.*;
import com.ousl.graduation_completion.repository.ProgramCriterionRepository;
import com.ousl.graduation_completion.repository.ProgramRepository;
import com.ousl.graduation_completion.repository.RuleLogRepository;
import com.ousl.graduation_completion.repository.StudentStatusRepository;
import com.ousl.graduation_completion.service.OptionalCourseCreditCheckerService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class OptionalCourseCreditCheckerServiceImpl implements OptionalCourseCreditCheckerService {

    @PersistenceContext
    EntityManager em;

    @Autowired
    RuleLogRepository ruleLogRepository;

    @Autowired
    StudentStatusRepository studentStatusRepository;

    Logger logger = LoggerFactory.getLogger(OptionalCourseCreditCheckerServiceImpl.class);
    @Autowired
    private ProgramCriterionRepository programCriterionRepository;
    @Autowired
    private ProgramRepository programRepository;
    @Autowired
    private StatusRepository statusRepository;

    @Override
    @Transactional
    public List<?> checkCoursesNeedToBeConvertedLevelThree(Integer programId) {
        try {
            /*
             *  This query retrieves unique course_id and application_id from the student table where the program_id is equal to the provided :programmeId,
             *  the valid column is equal to false, the level column is equal to 3, the course_type column is equal to 5,
             *  and the course_id is not found in the result of the subquery that selects course_ids from the program_course table with a matching program_id.
             * */
            String sql = "select distinct course_id, application_id " +
                    "from student " +
                    "where program_id = :programmeId and valid=false and level=:level and course_type = 5 and course_id not in (select program_course.course_id from program_course where program_course.program_id= :programmeId)";
            Query query = em.createNativeQuery(sql);
            query.setParameter("programmeId", programId);
            query.setParameter("level", 3);


            List<Object[]> results = query.getResultList();


            if (!results.isEmpty()) {
                logger.info("some courses are not valid");
                List<OptionalCourseListResponeDto> response = new ArrayList<>();
                for (Object[] res : results) {
                    OptionalCourseListResponeDto optionalCourseListResponeDto = new OptionalCourseListResponeDto();
                    optionalCourseListResponeDto.setCourseId(res[0].toString());
                    optionalCourseListResponeDto.setApplicationId(res[1].toString());
                    response.add(optionalCourseListResponeDto);
                }
                return response;
            } else {
                updateConsidered(programId, 3);
                assignHighestCreditIfEnrolledMoreThanSix(programId, 3);
            }

        } catch (Exception e) {
            logger.error("Error! " + e.getMessage());


        }
        return null;
    }

    private void updateConsidered(Integer programId, int level) {
        try {


            String updateAll = "update student set valid=false,last_updated_at=:current where  course_type=5  and level=:level and  program_id = :programmeId";
            Query nativeQuery = em.createNativeQuery(updateAll);
            nativeQuery.setParameter("programmeId", programId);
            nativeQuery.setParameter("level", level);
            nativeQuery.setParameter("current", LocalDateTime.now());

            int executed = nativeQuery.executeUpdate();
            logger.info("Success ! Update considered open elective " + executed + " to before");

            /*
                This query updates the 'valid' column of the 'student' table for specific conditions.
                It sets the 'valid' value to true for rows where the course_type is open elective,
                valid is currently false, level is 3, program_id matches the provided :programmeId,
                and the course_id is found in the result of a sub query that selects course_ids from
                the 'program_course' table for the corresponding program_id.
               */
            String updateSql = "update student set valid=true,last_updated_at=:current where  course_type=5 and valid=false and level=:level and  program_id = :programmeId  and course_id in (select program_course.course_id from program_course where program_course.program_id= :programmeId)";
            Query updatequery = em.createNativeQuery(updateSql);
            updatequery.setParameter("programmeId", programId);
            updatequery.setParameter("level", level);
            updatequery.setParameter("current", LocalDateTime.now());


            int executeUpdate = updatequery.executeUpdate();
            logger.info("Success ! Update considered open elective " + executeUpdate + " number of rows updated");
        } catch (Exception e) {
            logger.info("Error ! update Considered " + e.getMessage());
        }
    }

    private void assignHighestCreditIfEnrolledMoreThanSix(Integer programId, int level) {
        /*
         * If a student is enrolled in more than 6 credits, the function should assign the highest credit value to them.
         * */

        try {
            int updatedRows = 0;

            /*
            * This query retrieves the count of records from the "student" table. It counts the number of "id" values for rows that meet the following conditions:

                The "application_id" is found in the result of a sub query.
                The sub query selects "application_id" from the "student" table where:
                The "level" is 3.
                The "valid" column is true.
                The "program_id" matches the provided ":programId".
                The "course_type" is open elective.
                The "grade_map_id" is greater than or equal to 6 that means c or above.
                The results are grouped by "application_id".
                The sum of the "credit" column for each group is greater than 6.
            *
            *
            * */

            String countCreditForEnrolledStudent = "SELECT count(id)\n" +
                    "    FROM student where application_id in\n" +
                    "    (SELECT application_id\n" +
                    "        FROM student\n" +
                    "        where level = :level and valid = true and program_id = :programId and course_type = 5 and grade_map_id>=6 group by application_id HAVING sum(credit)>6\n" +
                    ") and level = :level and valid = true and program_id = :programId and course_type = 5 and grade_map_id>=6;";

            Query query = em.createNativeQuery(countCreditForEnrolledStudent);
            query.setParameter("programId", programId);
            query.setParameter("level", level);

            /*
             *  In this code, there is an infinite loop (while (true)) that continues until a specific condition is met.
             *  Within the loop, the code retrieves the result of executing a query using query.getSingleResult().
             *  The result is cast to Number and then converted to an int using .intValue().
             *
             * */
            while (true) {
                int numberOfRowsAffected = ((Number) query.getSingleResult()).intValue();

                /*
                 *  The variable numberOfRowsAffected represents the number of rows affected by the query.
                 *  The condition !(numberOfRowsAffected > 0) checks if the number of affected rows is not greater than 0.
                 *  If the condition evaluates to true,
                 *  indicating that no rows were affected, the break statement is executed,
                 *  exiting the loop.
                 * */

                if (!(numberOfRowsAffected > 0)) break;


                /*
                 *  This query updates the "valid" column of the "student" table to false for a specific condition.
                 *
                 *       It selects the "id" values from the "student" table where the level is 3, the "valid" column is true,
                 *       the "program_id" matches the provided ":programmeId",
                 *       the "course_type" is 5, and the "grade_map_id" is greater than or equal to 6.
                 *       The selected results are grouped by the "application_id" and only rows with a sum of "credit" greater than 6 are considered.
                 *       The results are ordered by "application_id" and "grade_map_id" in ascending order, and only the first row is selected using "LIMIT 1".
                 *
                 *  Finally, the "UPDATE" statement sets the "valid" column to false for the corresponding "id" values.
                 * */
                String updateSql =
                        "UPDATE student\n" +
                                "SET valid=false,last_updated_at=:current\n" +
                                "WHERE id IN (\n" +
                                "    SELECT id\n" +
                                "    FROM student where application_id in\n" +
                                "    (SELECT application_id\n" +
                                "        FROM student\n" +
                                "        where level = 3 and valid = true and program_id = :programmeId and course_type = 5 and grade_map_id>=6 group by application_id HAVING sum(credit)>6\n" +
                                ") and level = 3 and valid = true and program_id = :programmeId and course_type = 5 and grade_map_id>=6 ORDER BY application_id,grade_map_id asc LIMIT 1\n" +
                                ")";
                Query updatequery = em.createNativeQuery(updateSql);
                updatequery.setParameter("programmeId", programId);
                updatequery.setParameter("current", LocalDateTime.now());


                updatedRows += updatequery.executeUpdate();
            }
            logger.info("Success ! Students have been successfully assigned the highest credit value due to their enrollment in more than 6 credits. " + updatedRows + " number of rows updated as not considered.");


        } catch (Exception e) {
            logger.info("Error ! updateNotConsideredMoreThanSuggested" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public HashMap<String, Object> checkOpenElectiveCourseLevelThree(Long programId) {
        HashMap<String, Object> response = new HashMap<>();

        try {

            Optional<ProgramCriterion> programCriterion = programCriterionRepository.getProgramCriterionByProgram_IdAndActiveAndCriteria_Id(programId, true, 3L);

            if (programCriterion.isPresent()) {

                /*
                 *  This query inserts records into the student_failed_criteria_detail table.
                 *  It selects values from the program_id, application_id, and constant values 3, 'pass', and 'Subject has been successfully passed'.
                 *  The selected values are inserted into the corresponding columns program_id, student_id, criteria_id, status, and details of the student_failed_criteria_detail table.
                 * */
                String passSql = "INSERT INTO student_failed_criteria_detail (program_id, student_id, criteria_id, status, details)\n" +
                        "SELECT program_id,application_id, 3, 'pass', 'Open elctive cource has been successfully passed'\n" +
                        "FROM student \n" +
                        "WHERE program_id = :programId AND course_type = 5 AND valid = true AND grade_map_id >= 6\n " +
                        "GROUP BY program_id, application_id\n"+
                        "HAVING  sum(credit)>=6";

                Query passQuery = em.createNativeQuery(passSql);
                passQuery.setParameter("programId", programId);
                int passCount = passQuery.executeUpdate();
                logger.info("Success ! Updated passed students in criteria detail. " + passCount + " number of rows updated");

                /*

                 * This query inserts records into the student_failed_criteria_detail table.
                 * It selects values from the program_id, application_id, and constant values 3, 'fail', and 'Subject not passed'.
                 * The selected values are inserted into the corresponding columns program_id, student_id, criteria_id, status, and details of the student_failed_criteria_detail table.
                 *
                 * */

                String failSql =  "INSERT INTO student_failed_criteria_detail (program_id, student_id, criteria_id, status, details)\n" +
                        "SELECT program_id, application_id, 3, 'fail', 'Open elective course not passed'\n" +
                        "FROM student\n" +
                        "WHERE valid = true and program_id = :programId\n" +
                        "  AND course_type = 5\n" +
                        "and grade_map_id > 5\n" +
                        "GROUP BY program_id, application_id\n" +
                        "HAVING sum(credit) < 6";

                Query failQuery = em.createNativeQuery(failSql);
                failQuery.setParameter("programId", programId);
                int failCount = failQuery.executeUpdate();
                logger.info("Success ! Updated failed students in criteria detail. " + failCount + " number of rows updated");


                response.put("message", "success");
                response.put("passCount", passCount);
                response.put("failCount", failCount);

                Program program = programRepository.findById(programId).orElse(null);

                updateProgramSequence(program);
//                updateRuleLog(student,program);
//                studentStatus(program,student);
                return response;
            }
            response.put("message", "Already updated.");
            return response;

        } catch (Exception e) {
            response.put("message", "error : " + e.getMessage());
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

    private void studentStatus(Program program, Student student) {
        try {
            StudentStatus studentStatus = new StudentStatus();
            studentStatus.setProgram(program);
            Status status = statusRepository.findById(3L).orElse(null);
            studentStatus.setStatus(status);
            studentStatus.setStudent(student);
            studentStatusRepository.save(studentStatus);

        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Error ! Update student status " + e.getMessage());
        }
    }


}
