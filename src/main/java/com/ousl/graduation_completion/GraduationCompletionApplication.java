package com.ousl.graduation_completion;

import com.ousl.graduation_completion.repository.ApplicationRepository;
import com.ousl.graduation_completion.repository.GradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GraduationCompletionApplication {

	public static void main(String[] args) {
		SpringApplication.run(GraduationCompletionApplication.class, args);
	}

	@Autowired
	private ApplicationRepository applicationRepository;

	@Autowired
	private GradeRepository gradeRepository;

//	@Test
//	public void testApplicationRepository(){
//		System.out.println("test started");
//
//		applicationRepository.findAll().forEach(ap -> System.out.println(ap.getTitle()));
//
//	}
//
//	@Test
//	public void testGradeRepository(){
//		System.out.println("Started!!!");
//		gradeRepository.findAll().forEach(gr-> System.out.println(gr.getGarde()));
//
//	}
}
