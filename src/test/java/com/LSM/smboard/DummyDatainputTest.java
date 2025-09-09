package com.LSM.smboard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.LSM.smboard.question.QuestionService;

@SpringBootTest
public class DummyDatainputTest {

	@Autowired
	private QuestionService questionService;
	
	@Test
	public void testDummy() {
		for(int i=1; i<=300; i++) { //300개 질문더미 데이터생성
			String subject = String.format("테스트 데이터입니다:[%03d]", i);
			String content = "연습내용 더미데이터입니다";
			questionService.create(subject, content,null);
		}
		
	}
}
