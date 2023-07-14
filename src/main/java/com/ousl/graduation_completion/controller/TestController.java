package com.ousl.graduation_completion.controller;




import com.ousl.graduation_completion.dto.ContinueCourseDTO;
import com.ousl.graduation_completion.dto.DataObject;
import com.ousl.graduation_completion.models.Program;
import com.ousl.graduation_completion.repository.ProgramRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.formula.EvaluationWorkbook;
import org.apache.poi.ss.formula.udf.UDFFinder;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/data")
public class TestController {
@Autowired
ProgramRepository programRepository;

    @PersistenceContext
    EntityManager em;
    @GetMapping("/getAllProgrammes")
    public List<Program> getAllProgramme(){
    List<Program> programs = programRepository.findAll();
        return programs;
    }

//    @Transactional
//    @GetMapping("/checkCntCourse")
//    public ResponseEntity<?> checkCntCourse(@RequestParam("id") Integer progid){
//        String sql = "select distinct course_id, application_id from student where course_type= 3 and program_id = "+progid+" and valid=false  and course_id not in (select program_cnt_courses.course_id from program_cnt_courses where program_cnt_courses.program_id="+progid+" )";
//        Query query = em.createNativeQuery(sql);
//        List<Object[]> results = query.getResultList();
//        DataObject dataObject = new DataObject();
//        if(!results.isEmpty()){
//            List<ContinueCourseDTO> dataItems = new ArrayList<>();
//            for (Object[] result : results) {
//                Long  courseId = (Long) result[0];
//                Long applicationId = (Long) result[1];
//                ContinueCourseDTO dataItem = new ContinueCourseDTO();
//                dataItem.setCourseId(courseId);
//                dataItem.setApplicationId(applicationId);
//                dataItems.add(dataItem);
//            }
//            dataObject.setMessage("some courses are not valid");
//            dataObject.setData(dataItems);
//        }else{
//            String sql1 = "update student set valid=true where course_type=3 and program_id = "+progid+" and  valid=false and course_id in (select program_cnt_courses.course_id from program_cnt_courses where program_cnt_courses.program_id="+progid+" )";
//            Query query1 = em.createNativeQuery(sql1);
//            int updatedCount = query1.executeUpdate();
//            System.out.println("update count "+updatedCount);
//            dataObject.setMessage("All the Continue Courses are Considered");
//        }
//        return new ResponseEntity<>(dataObject,HttpStatus.OK);
//    }

    @Transactional
    @GetMapping("/checkCntCourse2")
    public ResponseEntity<?> checkCntCourse2(@RequestParam("id") Integer progid){
        String sql = "select count(course_id) as numcourses from program_cnt_courses where program_id="+progid+"";
        Query query = em.createNativeQuery(sql);
        Long count = (Long) query.getSingleResult();
        int numCourses = count.intValue();

        String sql1 = "INSERT INTO student_failed_criteria_detail (program_id, student_id, criteria_id, status, details)\n" +
                "SELECT program_id,application_id, 1, 'pass', NULL\n" +
                "FROM student \n" +
                "WHERE program_id = "+progid+" AND course_type = 3 AND valid = false AND grade_map_id >= 5\n" +
                "GROUP BY program_id, application_id\n" +
                "HAVING COUNT(course_id) >= "+numCourses+"";
        Query query1 = em.createNativeQuery(sql1);
        int updatedCount = query1.executeUpdate();


        String sql2 = "INSERT INTO student_failed_criteria_detail (program_id, student_id, criteria_id, status, details)\n" +
                "SELECT program_id,application_id, 1, 'fail', 'Not enough Continuious courses completed'\n" +
                "FROM student\n" +
                "WHERE program_id = "+progid+" AND course_type = 3 AND valid = false AND grade_map_id >= 5\n" +
                "GROUP BY program_id, application_id\n" +
                "HAVING COUNT(course_id) < "+numCourses+"";
        Query query2 = em.createNativeQuery(sql2);
        int updatedCount1 = query2.executeUpdate();

        return new ResponseEntity<>(updatedCount,HttpStatus.OK);
    }
//    INSERT INTO student_failed_criteria_detail (program_id, student_id, criteria_id, status, details)
//    SELECT program_id,1, 1, 'pass', NULL
//    FROM student
//    WHERE program_id = 164 AND course_type = 3 AND valid = false AND grade_map_id >= 5
//    GROUP BY program_id, application_id
//    HAVING COUNT(course_id) >= 3;





//    public ResponseEntity<byte[]> downloadExcel(List<Object[]> results) throws IOException {
//
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("CheckCntCourse");
//
//// Add course ID and application ID to the first row
//        Row headerRow = sheet.createRow(0);
//        Cell courseIdCell = headerRow.createCell(0);
//        courseIdCell.setCellValue("Course ID");
//        Cell applicationIdCell = headerRow.createCell(1);
//        applicationIdCell.setCellValue("Application ID");
//
//// Iterate over the results and add them to the Excel sheet
//        int rowNum = 1;
//        for (Object[] result : results) {
//            Row row = sheet.createRow(rowNum++);
//            int colNum = 0;
//            for (Object obj : result) {
//                Cell cell = row.createCell(colNum++);
//                cell.setCellValue(obj.toString());
//            }
//        }
//
//        // Get the Excel output stream
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        workbook.write(outputStream);
//
//        // Set the response headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        headers.setContentDisposition(ContentDisposition.parse("attachment; filename=checkCntCourse.xlsx"));
//
//        // Return the response
//        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
//    }

//    @GetMapping("/checkCntCourse")
//    public ResponseEntity<byte[]> downloadExcel(@RequestParam("id") Integer progid) throws IOException {
//        String sql = "select distinct course_id, application_id from student where course_type= 3 and program_id = "+progid+" and valid=false  and course_id not in (select program_cnt_courses.course_id from program_cnt_courses where program_cnt_courses.program_id="+progid+" )";
//        Query query = em.createNativeQuery(sql);
//        List<Object[]> results = query.getResultList();
//
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("CheckCntCourse");
//
//// Add course ID and application ID to the first row
//        Row headerRow = sheet.createRow(0);
//        Cell courseIdCell = headerRow.createCell(0);
//        courseIdCell.setCellValue("Course ID");
//        Cell applicationIdCell = headerRow.createCell(1);
//        applicationIdCell.setCellValue("Application ID");
//
//// Iterate over the results and add them to the Excel sheet
//        int rowNum = 1;
//        for (Object[] result : results) {
//            Row row = sheet.createRow(rowNum++);
//            int colNum = 0;
//            for (Object obj : result) {
//                Cell cell = row.createCell(colNum++);
//                cell.setCellValue(obj.toString());
//            }
//        }
//
//        // Get the Excel output stream
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//        workbook.write(outputStream);
//
//        // Set the response headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
//        headers.setContentDisposition(ContentDisposition.parse("attachment; filename=checkCntCourse.xlsx"));
//
//        // Return the response
//        return new ResponseEntity<>(outputStream.toByteArray(), headers, HttpStatus.OK);
//    }



}
