package com.LSM.smboard;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.LSM.smboard.entity.Question;
import com.LSM.smboard.repository.QuestionRepository;

@SpringBootTest
public class Test01 {

	@Autowired
	private QuestionRepository questionRepository;
	
//	@Test
//	@DisplayName("질문글 저장")
//	public void testJpa1() {
//		Question q1 = new Question();
//		q1.setSubject("ssb가 뭔가요");	
//		q1.setContent("ssb에 대해 알려주세요");
//		q1.setCreatedate(LocalDateTime.now());
//		//q1->entity 생성 완료
//		questionRepository.save(q1);
//		
//		Question q2 = new Question();
//		q2.setSubject("스프링부트가 뭔가요");	
//		q2.setContent("스프링부트에 대해 알려주세요");
//		q2.setCreatedate(LocalDateTime.now());
//		//q2->entity 생성 완료
//		questionRepository.save(q2);
//}
	@Test
	@DisplayName("모든 질문글 조회하기 테스트")
	public void testJpa2() {
		 List<Question> allQuestion = questionRepository.findAll();
		 assertEquals(2, allQuestion.size()); //예상 결과 확인하기(기대값,실제값)
		// System.out.println("기대값:"+allQuestion.size());
		 Question question = allQuestion.get(0);
		 assertEquals("sbb가 뭔가요", question.getSubject());
	}
	
	@Test
	@DisplayName("질문 글 번호(기본키인id)로 조회 테스트")
	public void testJpa3() {
		Optional<Question> qOptional = questionRepository.findById(1); //기본키로 조회-> 1번글 가져오기
		if(qOptional.isPresent()) { //참이면 기본키가 존재함
			Question question = qOptional.get(); //글꺼내기
			assertEquals("sbb가 뭔가요", question.getSubject());
		}
	}
}