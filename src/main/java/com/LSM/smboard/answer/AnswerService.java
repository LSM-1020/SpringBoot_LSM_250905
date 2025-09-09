package com.LSM.smboard.answer;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
