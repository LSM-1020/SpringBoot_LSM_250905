package com.LSM.smboard.answer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.LSM.smboard.question.Question;
import com.LSM.smboard.question.QuestionService;
@RequestMapping("/answer")
@Controller
public class AnswerController {
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private AnswerService answerService;
	
	@PostMapping(value="/create/{id}")	//답변 등록 요청->오는 파라미터 값은 부모 질문글의 번호
	public String createAnswer(Model model, @PathVariable("id") Integer id, @RequestParam("content") String content) {		
	Question question = questionService.getQuestion(id);
	
	answerService.create(question, content);
	return String.format("redirect:/question/detail/%s",id);
}
}