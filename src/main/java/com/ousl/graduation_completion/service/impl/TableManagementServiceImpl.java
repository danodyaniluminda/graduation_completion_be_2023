package com.ousl.graduation_completion.service.impl;

import com.ousl.graduation_completion.models.*;
import com.ousl.graduation_completion.repository.*;
import com.ousl.graduation_completion.service.TableManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TableManagementServiceImpl implements TableManagementService {

   private final TableRepository tableRepository;
   private final ApplicationRepository applicationRepository;
   private final StudentStatusRepository studentStatusRepository;
   private final StudentRepository studentRepository;
   private final ProgramCourseRepository programcourseRepository;
   private final StudentSubjectRepository studentsubjectRepository;
   private final GradeRepository gradeRepository;


   @Autowired
   public TableManagementServiceImpl(TableRepository tableRepository, ApplicationRepository applicationRepository, StudentStatusRepository studentStatusRepository, StudentRepository studentRepository, ProgramCourseRepository programcourseRepository, StudentSubjectRepository studentsubjectRepository, GradeRepository gradeRepository) {
        this.tableRepository = tableRepository;
        this.applicationRepository = applicationRepository;
        this.studentStatusRepository = studentStatusRepository;
        this.studentRepository = studentRepository;
        this.programcourseRepository = programcourseRepository;
        this.studentsubjectRepository = studentsubjectRepository;
        this.gradeRepository = gradeRepository;
   }

    @Override
    public List<ALLTables> getAllTables() {
        return tableRepository.findAll();
    }

@Override
public List<?> getAllDataFromTable(String table) {
        if ("student_status".equals(table)) {
            System.out.println("Fetching data from student_status table");
            return studentStatusRepository.findAll();
        } else if ("student".equals(table)) {
            System.out.println("Fetching data from student table");
            return studentRepository.findAll();
        }else if ("grade".equals(table)) {
            System.out.println("Fetching data from grade table");
            return gradeRepository.findAll();
        }
        else {
            System.out.println("Invalid table name: " + table);
            return null;
        }

    }

    @Override
    public ResponseEntity<String> updateCorrespondingTables(String tableName, List<Map<String, Object>> dataArray) {

        dataArray.forEach(data -> {

            // Program Course Table Update
            if ("program_course".equals(tableName)) {
                try {
                ProgramCourse programCourse = new ProgramCourse();
                System.out.println("working program course");

                if (data.containsKey("program_id")) {
                    Object pro_idValue = data.get("program_id");
                    Object course_idValue = data.get("course_id");


                    // send program_idvalue
                    if (pro_idValue instanceof Integer) {
                        Long PRO_ID = ((Integer) pro_idValue).longValue();
                        Program program = new Program();
                        program.setId(PRO_ID);
                        programCourse.setProgram(program);
                    } else if (pro_idValue instanceof String) {
                        Long PRO_ID = Long.parseLong((String) pro_idValue);
                        Program program = new Program();
                        program.setId(PRO_ID);
                        programCourse.setProgram(program);
                    }


                    // send Course value
                    if (course_idValue instanceof Integer) {
                        Long COURSE_ID = ((Integer) course_idValue).longValue();
                        Course course = new Course();
                        course.setId(COURSE_ID);
                        programCourse.setCourse(course);
                    } else if (course_idValue instanceof String) {
                        Long COURSE_ID = Long.parseLong((String) course_idValue);
                        Course course = new Course();
                        course.setId(COURSE_ID);
                        programCourse.setCourse(course);
                    }
                    programcourseRepository.save(programCourse);
                }else{
                    throw new RuntimeException("Wrong table name: " + tableName);
                }
                } catch (Exception ex) {
                        ex.printStackTrace();
                        // Handle exception for program_course table
                        throw new RuntimeException("Error updating program_course table");
                    }



            } else if ("student_subject".equals(tableName)) {
                try{
                StudentSubject studentSubject = new StudentSubject();
                System.out.println("working student subject");

                if (data.containsKey("type")) {
                    //Object idValue = data.get("id");
                    Object pro_idValue = data.get("program_id");
                    Object type_Value = data.get("type");
                    Object subject_idValue = data.get("subject_id");
                    Object student_idValue = data.get("student_id");


                    // send Program
                    if (pro_idValue instanceof Integer) {
                        Long PRO_ID = ((Integer) pro_idValue).longValue();
                        Program program = new Program();
                        program.setId(PRO_ID);
                        studentSubject.setProgram(program);
                    } else if (pro_idValue instanceof String) {
                        Long PRO_ID = Long.parseLong((String) pro_idValue);
                        Program program = new Program();
                        program.setId(PRO_ID);
                        studentSubject.setProgram(program);
                    }


                    // send Subject Type
                    if (type_Value instanceof Integer) {
                        Long SUB_TYPE = ((Integer) type_Value).longValue();
                        SubjectType subjecttype = new SubjectType();
                        subjecttype.setId(SUB_TYPE);
                        studentSubject.setType(subjecttype);
                        System.out.println("working student subject");
                    } else if (type_Value instanceof String) {
                        Long SUB_TYPE = Long.parseLong((String) type_Value);
                        SubjectType subjecttype = new SubjectType();
                        subjecttype.setId(SUB_TYPE);
                        studentSubject.setType(subjecttype);
                    }

                    //send subject id
                    if (subject_idValue instanceof Integer) {
                        Long SUBB_ID = ((Integer) subject_idValue).longValue();
                        studentSubject.setSubjectId(SUBB_ID);
                    } else if (subject_idValue instanceof String) {
                        Long SUBB_ID = Long.parseLong((String) subject_idValue);
                        studentSubject.setSubjectId(SUBB_ID);
                    }

                    //send student id
                    if (student_idValue instanceof Integer) {
                        Long STUDENT_ID = ((Integer) student_idValue).longValue();
                        studentSubject.setStudentId(STUDENT_ID);
                    } else if (subject_idValue instanceof String) {
                        Long STUDENT_ID = Long.parseLong((String) student_idValue);
                        studentSubject.setStudentId(STUDENT_ID);
                    }


                    studentsubjectRepository.save(studentSubject);
                }else{
                    throw new RuntimeException("Wrong table name: " + tableName);
                }
                }catch (Exception ex) {
                        ex.printStackTrace();
                        // Handle exception for program_course table
                        throw new RuntimeException("Error updating student_subject table");
                    }


            } else if ("student".equals(tableName)) {
                try{
                Student student = new Student();
                System.out.println("working student");
                if (data.containsKey("application_id")) {
                    Object app_idValue = data.get("application_id");
                    Object sub_idValue = data.get("subject_id");
                    Object course_idValue = data.get("course_id");
                    Object course_typeValue = data.get("course_type");
                    Object valid_Value = data.get("valid");
                    Object level_value = data.get("level");
                    Object credit_value = data.get("credit");
                    Object grade_map_value = data.get("grade_map_id");
                    Object program_idvalue = data.get("program_id");
                    Object gpv_value = data.get("gpv");

                    // send application id
                    if (app_idValue instanceof Integer) {  // if come integer format no worries to send as Long
                        Long APP_ID = ((Integer) app_idValue).longValue();
                        student.setApplicationId(APP_ID);
                    } else if (app_idValue instanceof String) {
                        Long APP_ID = Long.parseLong((String) app_idValue);  // if it comes with string format then cast to long value
                        student.setApplicationId(APP_ID);
                    }


                    // send subject id
                    if (sub_idValue instanceof Integer) {
                        Long SUB_ID = ((Integer) sub_idValue).longValue();
                        student.setSubjectId(SUB_ID);
                    } else if (sub_idValue instanceof String) {
                        Long SUB_ID = Long.parseLong((String) sub_idValue);
                        student.setSubjectId(SUB_ID);
                    }


                    // send course id
                    if (course_idValue instanceof Integer) {
                        Long COURSE_ID = ((Integer) course_idValue).longValue();
                        student.setCourseId(COURSE_ID);
                    } else if (course_idValue instanceof String) {
                        Long COURSE_ID = Long.parseLong((String) course_idValue);
                        student.setCourseId(COURSE_ID);
                    }


                    // send course type
                    if (course_typeValue instanceof Integer) {
                        Long COURSE_TYPE = ((Integer) course_typeValue).longValue();
                        student.setCourseType(COURSE_TYPE);
                    } else if (course_typeValue instanceof String) {
                        Long COURSE_TYPE = Long.parseLong((String) course_typeValue);
                        student.setCourseType(COURSE_TYPE);
                    }

                    // send valid status
                    Boolean VALID = null;
                    if (valid_Value instanceof Boolean) {
                        VALID = (Boolean) valid_Value;
                        student.setValid(VALID);
                    } else if (valid_Value instanceof String) {
                        VALID = Boolean.valueOf((String) valid_Value);
                        student.setValid(VALID);
                    }


                    // send Level
                    Integer LEVEL;
                    if (level_value instanceof Integer) {
                        LEVEL = (Integer) level_value;
                        student.setLevel(LEVEL);
                    } else if (level_value instanceof String) {
                        LEVEL = Integer.parseInt((String) level_value);
                        student.setLevel(LEVEL);
                    }

                    // send Level
                    Integer CREDIT;
                    if (credit_value instanceof Integer) {
                        CREDIT = (Integer) credit_value;
                        student.setCredit(CREDIT);
                    } else if (credit_value instanceof String) {
                        CREDIT = Integer.parseInt((String) credit_value);
                        student.setCredit(CREDIT);
                    }


                    // send grade map
                    if (grade_map_value instanceof Integer) {
                        Long GRADE_MAP_ID = ((Integer) grade_map_value).longValue();
                        Grade grade = new Grade();
                        grade.setId(GRADE_MAP_ID);
                        student.setGrade(grade);
                    } else if (grade_map_value instanceof String) {
                        Long GRADE_MAP_ID = Long.parseLong((String) grade_map_value);
                        Grade grade = new Grade();
                        grade.setId(GRADE_MAP_ID);
                        student.setGrade(grade);
                    }

                    // send program_idvalue
                    if (program_idvalue instanceof Integer) {
                        Long PROGRAM_ID = ((Integer) program_idvalue).longValue();
                        Program program = new Program();
                        program.setId(PROGRAM_ID);
                        student.setProgram(program);
                    } else if (program_idvalue instanceof String) {
                        Long PROGRAM_ID = Long.parseLong((String) program_idvalue);
                        Program program = new Program();
                        program.setId(PROGRAM_ID);
                        student.setProgram(program);
                    }


                    // send gpv
                    Double GPV;
                    if (gpv_value instanceof Integer) {
                        Integer intvalue = (Integer) gpv_value;
                        GPV = intvalue.doubleValue();
                        student.setGpv(GPV);
                    } else if (gpv_value instanceof String) {
                        GPV = Double.parseDouble((String) gpv_value);
                        student.setGpv(GPV);
                    } else if (gpv_value instanceof Double) {
                        GPV = (Double) gpv_value;
                        student.setGpv(GPV);
                    }

                    //save data
                    studentRepository.save(student);
                }else{
                    throw new RuntimeException("Wrong table name: " + tableName);
                }
                }catch (Exception ex) {
                    ex.printStackTrace();
                    // Handle exception for program_course table
                    throw new RuntimeException("Error updating student table");
                }

            } else {
                System.out.println("working else");
                throw new RuntimeException("Unknown table name: " + tableName);
            }
        });
        return ResponseEntity.ok().body("Tables updated successfully");
    }
}



