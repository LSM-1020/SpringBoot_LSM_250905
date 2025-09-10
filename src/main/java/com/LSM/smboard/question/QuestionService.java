package com.LSM.smboard.question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.LSM.smboard.DataNotFoundException;
import com.LSM.smboard.user.SiteUser;

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
	
	
	
	
}