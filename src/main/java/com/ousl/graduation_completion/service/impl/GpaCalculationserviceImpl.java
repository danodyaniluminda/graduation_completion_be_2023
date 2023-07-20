package com.ousl.graduation_completion.service.impl;

import com.ousl.graduation_completion.models.Program;
import com.ousl.graduation_completion.repository.ProgramRepository;
import com.ousl.graduation_completion.repository.StudentGpaRepository;
import com.ousl.graduation_completion.service.GpaCalculationService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class GpaCalculationserviceImpl implements GpaCalculationService {

    @Autowired
    StudentGpaRepository studentGpaRepository;

    @PersistenceContext
    EntityManager em;
    @Autowired
    private ProgramRepository programRepository;

    @Override
    @Transactional
    public HashMap<String, Object> gpaCalculations(Long programId) {

        HashMap<String, Object> response = new HashMap<>();
        try {
            Program program = programRepository.findById(programId).orElseThrow();
            String sql ="DELETE FROM student_gpa WHERE program_id=:programmeId";

            Query deleteQuery = em.createNativeQuery(sql);
            deleteQuery.setParameter("programmeId",programId);
            int executeDelete = deleteQuery.executeUpdate();

            response.put("status", true);
            response.put("message", "Records deleted successfully");

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


                response.put("status", true);
                response.put("message", "success");
            }

            if (program.getProgramType().equals("Special")){
                String sqlSpecial = "INSERT INTO student_gpa (program_id, student_id, gpa)\n" +
                        "SELECT program_id,application_id,\n" +
                        "CAST(SUM(credit*gpv*l)/(SUM(credit)*l)AS DECIMAL(7,2)) AS GPA,l\n" +
                        "FROM student s\n" +
                        "LEFT JOIN level l2 on s.level = l2.level\n" +
                        "WHERE course_type <> 3\n" +
                        "AND program_id=:programmeId\n" +
                        "AND valid=true\n" +
                        "AND credit > 0\n" +
                        "GROUP BY program_id, application_id, l, s.level";

                Query query = em.createNativeQuery(sqlSpecial);
                query.setParameter("programmeId",programId);
                int executeUpdate = query.executeUpdate();

                response.put("status", true);
                response.put("message", "success");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("status", false);
            response.put("message", "error : " + e.getLocalizedMessage());
        }

        return response;
    }

}
