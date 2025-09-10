package com.LSM.smboard.question;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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

import com.LSM.smboard.answer.AnswerForm;
import com.LSM.smboard.answer.AnswerService;
import com.LSM.smboard.user.SiteUser;
import com.LSM.smboard.user.UserService;

import jakarta.validation.Valid;

@RequestMapping("/question") //prefix(접두사)
@Controller
public class QuestionController {

    private final AnswerService answerService;
	
//	@Autowired
//	private QuestionRepository questionRepository;
	
	@Autowired
	private QuestionService questionService;
	
	@Autowired
	private UserService userService;


    QuestionController(AnswerService answerService) {
        this.answerService = answerService;
    }
	
	
	@GetMapping(value = "/list")
	//@ResponseBody
	//public String list(Model model, @RequestParam(value="page", defaultValue = "0")int page) {
	public String list(Model model) {	
		//List<Question> questionList = questionRepository.findAll(); //모든 질문글 불러오기
		List<Question> questionList = questionService.getList();
		//Page<Question> paging = questionService.getList(page);
		//게시글을 10개씩 자른 리스트
		model.addAttribute("questionList", questionList);
		//model.addAttribute("paging", paging);
		
		return "question_list";
	}	

	@GetMapping(value = "/detail/{id}") //파라미터이름 없이 값만 넘어왔을때 처리
	public String detail(Model model, @PathVariable("id") Integer id, AnswerForm answerForm) {
		
		//service에 3을 넣어서 호출
		Question question = questionService.getQuestion(id);
		model.addAttribute("question", question);
		return "question_detail"; //타임리프 html의 이름
	}
	@PreAuthorize("isAuthenticated()")
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
	@PreAuthorize("isAuthenticated()") //권한 설정, form에서 action으로 넘어오지 않으면 권한인증이 안됨
	@PostMapping(value = "/create") //질문 내용을 DB에 저장하는 메서드->POST
	public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {		
		
		if(bindingResult.hasErrors()) { //참이면 -> 유효성 체크에서 에러 발생
			return "question_form"; //에러 발생 시 다시 질문 등록 폼으로 이동
		}
		SiteUser siteUser = userService.getUser(principal.getName());
		//현재 로그인된 username으로 siteuser
		questionService.create(questionForm.getSubject(), questionForm.getContent(),siteUser); //질문 DB에 등록하기
		
		return "redirect:/question/list"; //질문 리스트로 이동->반드시 redirect
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value="/modify/{id}")
	public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id, Principal principal) {
		Question question = questionService.getQuestion(id);//아이디에 해당하는 엔티티 반환->수정하려는 글의 엔티티
		//글쓴 유저와 로그인한 유저의 동일 여부 재확인
		if(!question.getAuthor().getUsername().equals(principal.getName())) { //참이면 수정권한 없음
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다");
		}
		//questionForm에 subject와 content를 value값을 출력하는 기능이 이미 구현되어있어서 
		//해당폼을 재활용하기 위해 questionFrom에 question필드값을 저장하여 전송
		//question_Form에 subject,content글 불러오는 기능이 있음
		questionForm.setSubject(question.getSubject());
		questionForm.setContent(question.getContent());
		
		return "question_form";
	}
	@PreAuthorize("isAuthenticated()")
	@PostMapping(value="/modify/{id}")
	public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,Principal principal,@PathVariable("id") Integer id) {
		if(bindingResult.hasErrors()) {
			return "question_form";
		}
		
		Question question = questionService.getQuestion(id);
		//글쓴 유저와 로그인한 유저의 동일 여부 재확인
				if(!question.getAuthor().getUsername().equals(principal.getName())) { //참이면 수정권한 없음
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"수정권한이 없습니다");
				}
				questionService.modify(question,questionForm.getSubject(),questionForm.getContent());
		return String.format("redirect:/question/detail/%s", id);
	}
	@PreAuthorize("isAuthenticated()")
	@GetMapping(value="/delete/{id}")
	public String delete(Principal principal,@PathVariable("id") Integer id) {
		
		Question question = questionService.getQuestion(id);
		//글쓴 유저와 로그인한 유저의 동일 여부 재확인
				if(!question.getAuthor().getUsername().equals(principal.getName())) { //참이면 수정권한 없음
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"삭제권한이 없습니다");
				}
				questionService.delete(question);
				return "redirect:/question/list";
	}
}


