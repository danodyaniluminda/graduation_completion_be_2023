package com.ousl.graduation_completion.service.impl;

import com.ousl.graduation_completion.dto.ContinueCourseDTO;
import com.ousl.graduation_completion.dto.DataObject;
import com.ousl.graduation_completion.models.Program;
import com.ousl.graduation_completion.models.ProgramCriterion;
import com.ousl.graduation_completion.repository.ProgramCriterionRepository;
import com.ousl.graduation_completion.repository.ProgramRepository;
import com.ousl.graduation_completion.service.ContinuingCourseCheckerService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContinuingCourseCheckerServiceImpl implements ContinuingCourseCheckerService {
    @PersistenceContext
    EntityManager em;

    @Autowired
    OpenElectiveCourseCreditCheckerServiceImpl openElectiveCourseCreditCheckerService;

    @Autowired
    private ProgramCriterionRepository programCriterionRepository;
    @Autowired
    private ProgramRepository programRepository;
    @Override
    @Transactional
    public DataObject checkCntCourse(Integer progid) {
        String sql = "select distinct course_id, application_id from student where course_type= 3 and program_id = " + progid + " and valid=false  and course_id not in (select program_cnt_courses.course_id from program_cnt_courses where program_cnt_courses.program_id=" + progid + " )";
        Query query = em.createNativeQuery(sql);
        List<Object[]> results = query.getResultList();
        DataObject dataObject = new DataObject();

        if (!results.isEmpty()) {
            List<ContinueCourseDTO> dataItems = new ArrayList<>();
            for (Object[] result : results) {
                Long courseId = (Long) result[0];
                Long applicationId = (Long) result[1];
                ContinueCourseDTO dataItem = new ContinueCourseDTO();
                dataItem.setCourseId(courseId);
                dataItem.setApplicationId(applicationId);
                dataItems.add(dataItem);
            }
            dataObject.setMessage("some courses are not valid");
            dataObject.setStatus("NOT_MATCH");
            dataObject.setData(dataItems);
        } else {

            String sql1 = "update student set valid=true where course_type=3 and program_id = " + progid + " and  valid=false and course_id in (select program_cnt_courses.course_id from program_cnt_courses where program_cnt_courses.program_id=" + progid + " )";
            Query query1 = em.createNativeQuery(sql1);
            int updatedCount = query1.executeUpdate();
            System.out.println("update count " + updatedCount);
            dataObject.setMessage("All the Continue Courses are Considered");
            dataObject.setStatus("SUCCESS");
        }

        return dataObject;
    }

    @Override
    @Transactional
    public DataObject updateFailedOrPassedCritiaStudent(Integer progid) {
        Optional<ProgramCriterion> programCriterion = programCriterionRepository.getProgramCriterionByProgram_IdAndActiveAndCriteria_Id(Long.valueOf(progid), true, 1L);
        if (programCriterion.isPresent()) {
            DataObject dataObject = new DataObject();
            String sql = "select count(course_id) as numcourses from program_cnt_courses where program_id=" + progid;
            Query query = em.createNativeQuery(sql);
            Long count = (Long) query.getSingleResult();
            int numCourses = count.intValue();

            String sql3 = "select count(id) as totalRecord FROM student where\n" +
                    "course_type=3 and program_id=164 and valid=true";
            Query query3 = em.createNativeQuery(sql3);
            Object result = query3.getSingleResult();
            Long totalRecord = ((Number) result).longValue();

            String sql1 = "INSERT INTO student_failed_criteria_detail (program_id, student_id, criteria_id, status, details)\n" +
                    "SELECT program_id,application_id, 1, 'pass', NULL\n" +
                    "FROM student \n" +
                    "WHERE program_id = " + progid + " AND course_type = 3 AND valid = true AND grade_map_id >= 5\n" +
                    "GROUP BY program_id, application_id\n" +
                    "HAVING COUNT(course_id) >= " + numCourses;
            Query query1 = em.createNativeQuery(sql1);
            int updatedCount = query1.executeUpdate();

            String sql2 = "INSERT INTO student_failed_criteria_detail (program_id, student_id, criteria_id, status, details)\n" +
                    "SELECT program_id,application_id, 1, 'fail', 'Not enough Continuious courses completed'\n" +
                    "FROM student\n" +
                    "WHERE program_id = " + progid + " AND course_type = 3 AND valid = true AND grade_map_id >= 5\n" +
                    "GROUP BY program_id, application_id\n" +
                    "HAVING COUNT(course_id) < " + numCourses;
            Query query2 = em.createNativeQuery(sql2);
            int updatedCount2 = query2.executeUpdate();


            dataObject.setStatus("SUCCESS");
            dataObject.setMessage("Total of Consider Data - " + totalRecord + ", Pass - " + updatedCount + " recoards, Fail - " + updatedCount2 + " recoards");
            Program program = programRepository.findById(Long.valueOf(progid)).orElse(null);
            openElectiveCourseCreditCheckerService.updateProgramSequence(program);
            return dataObject;
        }else{
            DataObject dataObject = new DataObject();
            dataObject.setStatus("SUCCESS");
            dataObject.setMessage("Already updated.");
            return dataObject;
        }
    }


    @Override
    @Transactional
    public DataObject critria(Integer progid) {
        DataObject dataObject = new DataObject();

        String sql1 = "INSERT INTO student_course_list (application_id, course_id, program_id)\n" +
                "SELECT\n" +
                "application_id,\n" +
                "array_agg(student.course_id) AS stdcourses,\n" +
                "program_id\n" +
                "FROM\n" +
                "student\n" +
                "WHERE\n" +
                "student.program_id = "+progid+" and student.valid = true\n" +
                "GROUP BY\n" +
                "application_id, program_id";
        Query query1 = em.createNativeQuery(sql1);
        int updatedCount = query1.executeUpdate();

        String sql2 = "insert into student_with_mutual_ex_courses (application_id,mutual_ex_course_id,has_mutual_course_done)\n" +
                "SELECT application_id,mutualexclusivecourse.id,true from student_course_list,mutualexclusivecourse\n" +
                "WHERE student_course_list.program_id = mutualexclusivecourse.programme_id\n" +
                "and mutualexclusivecourse.programme_id = "+progid+" \n" +
                "and arraycontains(student_course_list .course_id,mutualexclusivecourse.mutuale_course_id)\n" +
                "and arraycontains(student_course_list .course_id,mutualexclusivecourse.course_id)";
        Query query2 = em.createNativeQuery(sql2);
        int updatedCount2 = query2.executeUpdate();


        dataObject.setStatus("SUCCESS");
        dataObject.setMessage("Query Work");
        return dataObject;
    }
}

