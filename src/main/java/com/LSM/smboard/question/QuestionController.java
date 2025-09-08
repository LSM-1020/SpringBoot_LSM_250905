package com.LSM.smboard.question;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


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
}
