package com.LSM.smboard.question;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.validation.Valid;


@RequestMapping("/question")
@Controller
public class QuestionController {
//	@Autowired
//	private QuestionRepository questionRepository;
	
	@Autowired
	private QuestionService questionService;
	
	@GetMapping(value="/")
	public String root() {
		return "redirect:/question/list";
	}

	@GetMapping(value = "/list")
	//@ResponseBody // return에 적은 문자열이 그대로 출력됨
	public String list(Model model) {
		
		 //List<Question> questionlist = questionRepository.findAll();
		List<Question> questionlist = questionService.getList();
		 model.addAttribute("questionList",questionlist);
		
		return "question_list";
	}
	@GetMapping(value="/detail/{id}") //파라미터 이름 없이 값만 넘어왔을때 처리, {id}가 "id"로 가서 최종 integer id로 
	public String detail(Model model, @PathVariable("id") Integer id) {
		//service에 3을 넣어서 호출
		
		Question question = questionService.getQuestion(id);
		model.addAttribute("question",question);
		return "question_detail"; //타임리프 html의 이름
	}
	@GetMapping(value="/create") // 질문등록 폼만 매핑해주는 메소드 get
	public String questionCreate(QuestionForm questionForm) {
		
		return "question_form"; //
	}
//	@PostMapping(value="/create") //질문내용을 db에 저장하는 메소드 post
//	public String questionCreate(@RequestParam(value="subject") String subject, @RequestParam(value="content") String content) {
//		//@Requestparam("subject") String subject -> ruquest.getparameter("subject") 
//		//@Requestparam("content") String content -> ruquest.getparameter("content")
//		questionService.create(subject, content);
//		
//		return "redirect:/question/list"; //
//	}
	@PostMapping(value="/create") // 질문내용을 db에 저장하는 메소드 post
	public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult) {
		//@Requestparam("subject") String subject -> ruquest.getparameter("subject") 
		//@Requestparam("content") String content -> ruquest.getparameter("content")
		if(bindingResult.hasErrors()) { //참이면 에러가 있다
			return "question_form";
		}
		questionService.create(questionForm.getSubject(), questionForm.getContent());
		
		return "redirect:/question/list"; //
	}
}
