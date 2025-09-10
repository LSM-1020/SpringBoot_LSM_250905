package com.LSM.smboard.answer;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.LSM.smboard.question.Question;
import com.LSM.smboard.question.QuestionService;
import com.LSM.smboard.user.SiteUser;
import com.LSM.smboard.user.UserService;

import jakarta.validation.Valid;
@RequestMapping("/answer")
@Controller
public class AnswerController {
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private AnswerService answerService;
	
	@Autowired
	private UserService userService;
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/create/{id}") //답변 등록 요청->오는 파라미터 값 : 부모 질문글의 번호
	public String createAnswer(Model model, @PathVariable("id") Integer id, @Valid AnswerForm answerForm, BindingResult bindingResult, Principal principal) {
		Question question = questionService.getQuestion(id);		
		SiteUser siteUser = userService.getUser(principal.getName()); //로그인한 유저의 아이디 얻기
		
		if(bindingResult.hasErrors()) {
			model.addAttribute("question", question);
			return "question_detail";
		}
		
		answerService.create(question, answerForm.getContent(), siteUser); //DB에 답변 등록
		
		return String.format("redirect:/question/detail/%s", id);
	}
	
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/modify/{id}") //답변을 수정할수있는 form으로 이동하는 요청
	public String answerModify(AnswerForm answerForm, @PathVariable("id") Integer id, Principal principal) {
		Answer answer = answerService.getAnswer(id);
		//글쓴 유저와 로그인한 유저의 동일 여부 재확인
				if(!answer.getAuthor().getUsername().equals(principal.getName())) { //참이면 수정권한 없음
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정 권한이 없습니다");
				}
		answerForm.setContent(answer.getContent());
		
				return "answer_form";
	}
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value = "/modify/{id}") //질문 수정하기위한 요청
	public String answer(@Valid AnswerForm answerForm,BindingResult bindingResult, @PathVariable("id") Integer id, Principal principal) {
		if(bindingResult.hasErrors()) {
			return "answer_form";
		}
		Answer answer = answerService.getAnswer(id);//수정전 원본답변 엔티티
		if(!answer.getAuthor().getUsername().equals(principal.getName())) { //참이면 수정권한 없음
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정 권한이 없습니다");
		}
		answerService.modify(answer, answerForm.getContent()); //수정 완료
		return String.format("redirect:/question/detail/%s", answer.getQuestion().getId());
		//redirect->부모글(해당 답변이 달린 질문글)의 번호로 이동
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value = "/delete/{id}") //질문 수정하기위한 요청
	public String answerdelete(@PathVariable("id") Integer id, Principal principal) {
		
		Answer answer = answerService.getAnswer(id);
		if(!answer.getAuthor().getUsername().equals(principal.getName())) { //참이면 수정권한 없음
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정 권한이 없습니다");
		}
		answerService.delete(answer);
		return String.format("redirect:/question/detail/%s", answer.getQuestion().getId()); //원글로 돌아감
		//redirect->부모글(해당 답변이 달린 질문글)의 번호로 이동
	}
}