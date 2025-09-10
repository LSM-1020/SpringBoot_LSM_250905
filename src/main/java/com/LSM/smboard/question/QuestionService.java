package com.LSM.smboard.question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.LSM.smboard.DataNotFoundException;
import com.LSM.smboard.user.SiteUser;

import ch.qos.logback.core.model.Model;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {

	
	private final QuestionRepository questionRepository;
	//@requiredArgsconstructor에 의해 생성자 방식으로 주입된 questionRepository (final필드만 가능)
	
	public List<Question> getList() {//모든 질문글 가져오기->페이징
		return this.questionRepository.findAll();
	}
	
	public Question getQuestion(Integer id) {
		Optional<Question> qOptional = questionRepository.findById(id);
		if(qOptional.isPresent()) {
			return qOptional.get(); //question반환
		} else {
			throw new DataNotFoundException("question not found");
		}
	}
	
	public void create(String subject, String content, SiteUser author) {
		Question question = new Question();
		question.setSubject(subject);
		question.setContent(content);
		question.setCreatedate(LocalDateTime.now());
		question.setAuthor(author);
		questionRepository.save(question);
		
	}
	public void modify(Question question,String subject, String content) {
		
		question.setContent(content);
		question.setSubject(subject);
		question.setModifydate(LocalDateTime.now());
		questionRepository.save(question);
	}
	public void delete(Question question) {
		questionRepository.delete(question);
	}
	
	public void vote(Question question, SiteUser siteUser) { //업데이트문으로 만들어줘야함
		question.getVoter().add(siteUser);
		//question은 추천을 받은 글의 번호로 조회한 엔티티
		//question의 멤버인 voter를 get해서 voter에 추천을 누른 유저의 엔티티를 추가
		questionRepository.save(question); //추천한 유저수가 변경된 질문 엔티티를 다시 save해서 갱신
	}
	
	public void disvote(Question question, SiteUser siteUser) { //업데이트문으로 만들어줘야함
		question.getDisvoter().add(siteUser);
		//question은 추천을 받은 글의 번호로 조회한 엔티티
		//question의 멤버인 voter를 get해서 voter에 추천을 누른 유저의 엔티티를 추가
		questionRepository.save(question); //추천한 유저수가 변경된 질문 엔티티를 다시 save해서 갱신
	}
	
	public void hit(Question question) { //조회수 증가questionRepository.updateHit(id);
		question.setHit(question.getHit()+1);
		questionRepository.save(question);
	}
	
}