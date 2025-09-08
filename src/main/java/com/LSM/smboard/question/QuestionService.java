package com.LSM.smboard.question;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.LSM.smboard.DataNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionService {

	
	private final QuestionRepository questionRepository;
	//@requiredArgsconstructor에 의해 생성자 방식으로 주입된 questionRepository (final필드만 가능)
	
	public List<Question> getList() {//모든 질문글 가져오기
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
}