package com.LSM.smboard;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.LSM.smboard.entity.Answer;
import com.LSM.smboard.entity.Question;
import com.LSM.smboard.repository.AnswerRepository;
import com.LSM.smboard.repository.QuestionRepository;

@SpringBootTest
public class Test02 {

	@Autowired
	private QuestionRepository questionRepository;
	@Autowired
	private AnswerRepository answerRepository;
	
	@Test
	@DisplayName("질문게시판 제목 수정하기")
	public void testJpa1() {
		Optional<Question> optionalQuestion = questionRepository.findById(2);
		assertTrue(optionalQuestion.isPresent()); //기본키로 가져온 레코드가 존재하면 trute, 테스트 통과
		//기본키로 가져온 레코드가 존재하지 않으면 false 테스트 종료(실패)
		Question question = optionalQuestion.get();
		question.setSubject("수정된 제목");
		this.questionRepository.save(question);
		
	}
//	@Test
//	@DisplayName("질문게시판 글 삭제하기")
//	public void testJpa2() {
//		assertEquals(16, questionRepository.count());
//		//questionRepository.count()-> 모든 레코드의 갯수 반환
//		Optional<Question> QOptional = questionRepository.findById(1); //1번글 가져오기
//		assertTrue(QOptional.isPresent()); //1번글의 존재 여부, 트루면 성공
//		Question question = QOptional.get();
//		questionRepository.delete(question); //delete(엔티티) -> 해당글 삭제
//		assertEquals(15, questionRepository.count());
//	}
	
//	@Test
//	@DisplayName("답변 게시판 글 생성하기")
//	public void testJpa3() {
//		//답변은 질문글의 번호를 준비해야함 부모글의 답변이기 때문에
//		Optional<Question> QOptional = questionRepository.findById(3);
//		assertTrue(QOptional.isPresent());
//		Question question = QOptional.get();
//		
//		Answer answer = new Answer();
//		answer.setContent("네 자동으로 생성됩니다"); //답변 내용 넣어주기
//		answer.setCreatedate(LocalDateTime.now()); //현재 시간 넣어주기
//		answer.setQuestion(question); //부모글이 있어야 답글도 존재, 답변이 달릴 질문글 필드로 넣어주기
//		answerRepository.save(answer);
//		
//	}
	
	@Test
	@DisplayName("답변 게시판 글 조회하긴")
	public void testJpa4() {
		Optional<Answer> AOptional = answerRepository.findById(1); //답변글 테이블에서 1번글 가져오기
		assertTrue(AOptional.isPresent());
		
		Answer answer = AOptional.get();
		assertEquals(3, answer.getQuestion().getId());//부모 질문글의 번호로 확인 테스트
		
	}
	
	@Test
	@DisplayName("질문글을 통해 답변 글들 조회하기")
	@Transactional
	public void testJpa5() {
		//질문글 가져오기(3번글)
		Optional<Question> QOptional = questionRepository.findById(3); //3번글 찾기
		assertTrue(QOptional.isPresent()); //3번글 있으면 true
		Question question = QOptional.get(); //question 3번글 선언
		
		List<Answer> answerList = question.getAnswerList(); //답변 목록 가져오기
		//게으른 초기화문제로 오류-> question entity가 닫힌 후에 초기화를 시도
		//테스트 과정에서만 발생 @transactional로 에러는 막을수있음
		
		assertEquals(1, answerList.size()); //답변객체 1개 예상, 실제 객채 -> 답변글 갯수가 예상과 실제가 같다면 true
		assertEquals("네 자동으로 생성됩니다",answerList.get(0).getContent());//답변이 네 자동으로 생성됩니다와 같으면 true
		
	}
	
}
