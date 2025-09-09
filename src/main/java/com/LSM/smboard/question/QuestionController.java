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

import com.LSM.smboard.answer.AnswerForm;

import jakarta.validation.Valid;

@RequestMapping("/question") //prefix(접두사)
@Controller
public class QuestionController {
	
//	@Autowired
//	private QuestionRepository questionRepository;
	
	@Autowired
	private QuestionService questionService;
	
	@GetMapping(value = "/") //root 요청 처리
	public String root() {
		return "redirect:/question/list";
	}
	
	@GetMapping(value = "/list")
	//@ResponseBody
	public String list(Model model) {
		
		//List<Question> questionList = questionRepository.findAll(); //모든 질문글 불러오기
		List<Question> questionList = questionService.getList();
		model.addAttribute("questionList", questionList);
		
		return "question_list";
	}	
	
	@GetMapping(value = "/detail/{id}") //파라미터이름 없이 값만 넘어왔을때 처리
	public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
		
		//service에 3을 넣어서 호출
		Question question = questionService.getQuestion(id);
		model.addAttribute("question", question);
		return "question_detail"; //타임리프 html의 이름
	}
	
	@GetMapping(value = "/create") //질문 등록 폼만 매핑해주는 메서드->GET
	public String questionCreate(QuestionForm questionForm) {
		return "question_form"; //질문 등록하는 폼 페이지 이름
	}
	
//	@PostMapping(value = "/create") //질문 내용을 DB에 저장하는 메서드->POST
//	public String questionCreate(@RequestParam(value = "subject") String subject, @RequestParam(value = "content") String content) {
//		//@RequestParam("subject") String subject-> String subject = request.getParameter("subject")
//		//@RequestParam("content") String content-> String content = request.getParameter("content")
//		
//		questionService.create(subject, content); //질문 DB에 등록하기
//		
//		return "redirect:/question/list"; //질문 리스트로 이동->반드시 redirect
//	}
	
	@PostMapping(value = "/create") //질문 내용을 DB에 저장하는 메서드->POST
	public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult) {		
		
		if(bindingResult.hasErrors()) { //참이면 -> 유효성 체크에서 에러 발생
			return "question_form"; //에러 발생 시 다시 질문 등록 폼으로 이동
		}
		
		questionService.create(questionForm.getSubject(), questionForm.getContent()); //질문 DB에 등록하기
		
		return "redirect:/question/list"; //질문 리스트로 이동->반드시 redirect
	}
}