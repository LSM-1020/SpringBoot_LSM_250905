package com.LSM.smboard.answer;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.LSM.smboard.DataNotFoundException;
import com.LSM.smboard.question.Question;
import com.LSM.smboard.user.SiteUser;

@Service
public class AnswerService {
	@Autowired
	private AnswerRepository answerRepository;
	
	public void create(Question question, String content, SiteUser author) {
		Answer answer = new Answer();
		answer.setContent(content);
		answer.setQuestion(question);
		answer.setCreatedate(LocalDateTime.now());
		answer.setAuthor(author);
		answerRepository.save(answer);
	}
	public Answer getAnswer(Integer id) { //기본키인 답변id를 인수로 넣어주면 해당 엔티티 반환
		Optional<Answer> _answer = answerRepository.findById(id); //기본키를 엔티티로 조회
		if(_answer.isPresent()) {
			return _answer.get(); //해당 answer엔티티 반환
		} else {
			throw new DataNotFoundException("해당 답변이 존재하지 않습니다");
		}
	}
	
	public void modify(Answer answer, String content) { //답변 수정하기
		answer.setContent(content);
		answer.setModifydate(LocalDateTime.now()); //답변수정 일시
		answerRepository.save(answer);
	}
	public void delete(Answer answer) {
		answerRepository.delete(answer);
	}
}
