package com.ousl.graduation_completion.service.impl;

import com.ousl.graduation_completion.dto.DataObject;
import com.ousl.graduation_completion.models.*;
import com.ousl.graduation_completion.repository.ProgramCriterionRepository;
import com.ousl.graduation_completion.repository.ProgramRepository;
import com.ousl.graduation_completion.repository.StudentGpaRepository;
import com.ousl.graduation_completion.repository.StudentStatusRepository;
import com.ousl.graduation_completion.service.GpaCalculationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class GpaCalculationserviceImpl implements GpaCalculationService {

    @Autowired
    StudentGpaRepository studentGpaRepository;

    Logger logger = LoggerFactory.getLogger(GpaCalculationserviceImpl.class);

    @Autowired
    private ProgramCriterionRepository programCriterionRepository;

    @PersistenceContext
    EntityManager em;

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    StudentStatusRepository studentStatusRepository;


    @Override
    @Transactional
    public DataObject checkConsideredApplications(Long programId) {

    //Calculation, no of records are considerd for GPA calculation

            DataObject dataObject = new DataObject();
            String sqlCheckApplications = "SELECT count(application_id)\n" +
                    "FROM student\n" +
                    "WHERE course_type <> 3\n" +
                    "AND valid= true\n" +
                    "AND credit > 0\n" +
                    "AND program_id = :programmeId";

            Query query = em.createNativeQuery(sqlCheckApplications);
            query.setParameter("programmeId", programId);
//          int total = query.executeUpdate();
            Object result = query.getSingleResult();
            Long totalRecord = ((Number) result).longValue();

            dataObject.setStatus("Success");
            dataObject.setMessage(" Success " + totalRecord + " no of records are considered ");

            return dataObject;

    }


    @Override
    @Transactional
    public HashMap<String, Object> gpaCalculations(Long programId) {

        HashMap<String, Object> response = new HashMap<>();
        try {

            Optional<ProgramCriterion> programCriterion = programCriterionRepository.getProgramCriterionByProgram_IdAndActiveAndCriteria_Id(programId, true, 3L);

            if (programCriterion.isPresent()) {

                Program program = programRepository.findById(programId).orElseThrow();

//    Clear previous records From student_gpa table
                String sql ="DELETE FROM student_gpa WHERE program_id=:programmeId";

                Query deleteQuery = em.createNativeQuery(sql);
                deleteQuery.setParameter("programmeId",programId);
                int executeDelete = deleteQuery.executeUpdate();

//If program type General, Calculation GPA
                if (program.getProgramType().equals("General")) {
                    String sqlGeneral = "INSERT INTO student_gpa (program_id, student_id, gpa)\n" +
                            "SELECT program_id, application_id,\n" +
                            "CAST(SUM(credit*gpv)/sum(credit)as decimal(7,2)) as GPA\n" +
                            "FROM student WHERE program_id=:programmeId\n" +
                            "AND valid=true\n" +
                            "AND course_type <> 3\n" +
                            "AND credit > 0\n" +
                            "GROUP BY program_id, application_id";

                    Query query = em.createNativeQuery(sqlGeneral);
                    query.setParameter("programmeId",programId);
                    int executeUpdate = query.executeUpdate();

                }

//If program type Special, Calculation GPA
                if (program.getProgramType().equals("Special")){
                    String sqlSpecial = "INSERT INTO student_gpa (program_id, student_id, gpa)\n" +
                            "SELECT program_id,application_id,\n" +
                            "CAST(SUM(credit*gpv*l)/(SUM(credit)*l)AS DECIMAL(7,2)) AS gpa\n" +
                            "FROM student s, level\n" +
                            "WHERE course_type <> 3\n" +
                            "and s.level = level.l\n" +
                            "AND program_id=:programmeId\n" +
                            "AND valid=true\n" +
                            "AND credit > 0\n" +
                            "GROUP BY program_id, application_id, l";

                    Query query = em.createNativeQuery(sqlSpecial);
                    query.setParameter("programmeId",programId);
                    int executeUpdate = query.executeUpdate();

//                    List<StudentGpa> getInsertedStudents = studentGpaRepository.getAllByCriteria_IdAndStatusAndProgram(3L, "pass", program);
//                    studentStatus(4L,getInsertedStudents);
                    logger.info("Success ! " + executeUpdate + " number of rows updated");
                    updateProgramSequence(program);

                }
                return response;
            }
            response.put("message", "GPA calculations completed successfully.");
            return response;

        } catch (Exception e) {

            logger.error(" Error " + e.getMessage());
            response.put("message", "error : " + e.getMessage());
            return response;
//            e.printStackTrace();
//            response.put("status", false);
//            response.put("message", "error : " + e.getLocalizedMessage());
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

//            ProgramCriterion newProgramCriterion = programCriterionRepository.getProgramCriterionBySequenceIdAndProgram(programCriterion.getSequenceId() + 1, program);
//            if (newProgramCriterion != null) {
//                newProgramCriterion.setActive(true);
//                programCriterionRepository.save(newProgramCriterion);
//            }
            logger.info("Success ! Update Program Sequence");


        } catch (Exception e) {
            e.printStackTrace();
            logger.info("Error ! Update Program Sequence " + e.getMessage());
        }
    }

//    private void studentStatus(Long programCriterionId, Iterable<? extends StudentGpa> getInsertedStudents) {
//        try {
//
//            for (StudentGpa student : getInsertedStudents) {
//                StudentStatus studentStatus = new StudentStatus();
//                studentStatus.setStudent(student.getId());
//                studentStatus.setProgramCriterion(programCriterionId);
//                studentStatus.setCreateDate(Instant.now());
//                studentStatusRepository.save(studentStatus);
//            }
//            logger.info("Success ! updated student status ");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.info("Error ! Not update student status " + e.getMessage());
//        }
//    }



}
