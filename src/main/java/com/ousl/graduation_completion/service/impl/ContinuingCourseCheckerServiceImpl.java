package com.ousl.graduation_completion.service.impl;

import com.ousl.graduation_completion.dto.ContinueCourseDTO;
import com.ousl.graduation_completion.dto.DataObject;
import com.ousl.graduation_completion.service.ContinuingCourseCheckerService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ContinuingCourseCheckerServiceImpl implements ContinuingCourseCheckerService {
    @PersistenceContext
    EntityManager em;


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
            dataObject.setData(dataItems);
        } else {
            String sql1 = "update student set valid=true where course_type=3 and program_id = " + progid + " and  valid=false and course_id in (select program_cnt_courses.course_id from program_cnt_courses where program_cnt_courses.program_id=" + progid + " )";
            Query query1 = em.createNativeQuery(sql1);
            int updatedCount = query1.executeUpdate();
            System.out.println("update count " + updatedCount);
            dataObject.setMessage("All the Continue Courses are Considered");
        }

        return dataObject;
    }

    @Override
    public int updateFailedOrPassedCritiaStudent(Integer progid) {
        String sql = "select count(course_id) as numcourses from program_cnt_courses where program_id=" + progid;
        Query query = em.createNativeQuery(sql);
        Long count = (Long) query.getSingleResult();
        int numCourses = count.intValue();

        String sql1 = "INSERT INTO student_failed_criteria_detail (program_id, student_id, criteria_id, status, details)\n" +
                "SELECT program_id,application_id, 1, 'pass', NULL\n" +
                "FROM student \n" +
                "WHERE program_id = " + progid + " AND course_type = 3 AND valid = false AND grade_map_id >= 5\n" +
                "GROUP BY program_id, application_id\n" +
                "HAVING COUNT(course_id) >= " + numCourses;
        Query query1 = em.createNativeQuery(sql1);
        int updatedCount = query1.executeUpdate();

        String sql2 = "INSERT INTO student_failed_criteria_detail (program_id, student_id, criteria_id, status, details)\n" +
                "SELECT program_id,application_id, 1, 'fail', 'Not enough Continuious courses completed'\n" +
                "FROM student\n" +
                "WHERE program_id = " + progid + " AND course_type = 3 AND valid = false AND grade_map_id >= 5\n" +
                "GROUP BY program_id, application_id\n" +
                "HAVING COUNT(course_id) < " + numCourses;
        Query query2 = em.createNativeQuery(sql2);
        int updatedCount1 = query2.executeUpdate();

        return updatedCount;
    }
}

