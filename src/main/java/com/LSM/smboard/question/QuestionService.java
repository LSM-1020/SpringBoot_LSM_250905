package com.LSM.smboard.question;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.LSM.smboard.DataNotFoundException;
import com.LSM.smboard.answer.Answer;
import com.LSM.smboard.user.SiteUser;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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
	
	//페이징 테스트
		public Page<Question> getPageQuestions(int page, String kw) {
			
			//Specification<Question> spec = search(kw);
			int size = 10; //1페이지당 10개씩 글 출력
			
			int startRow = page * size;
			int endRow = startRow + size;
			
			//검색어 없이 리스트 조회
			List<Question> pageQuestionList = questionRepository.findQuestionsWithPaging(startRow, endRow);
			long totalQuestion = questionRepository.count(); //모든 글 갯수 가져오기
			
			//검색어로 검색결과 리스트 조회
			List<Question> searchQuestionList = questionRepository.searchQuestionsWithPaging(kw, startRow, endRow);
			int totalSearchQuestion = questionRepository.countSearchResult(kw);
			
			Page<Question> pagingList = new PageImpl<>(searchQuestionList, PageRequest.of(page, size), totalSearchQuestion);
			
			return pagingList;
			
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
	
	public void vote(Question question, SiteUser siteUser) { //업데이트문으로 만들어줘야함
		question.getVoter().add(siteUser);
		//question은 추천을 받은 글의 번호로 조회한 엔티티
		//question의 멤버인 voter를 get해서 voter에 추천을 누른 유저의 엔티티를 추가
		questionRepository.save(question); //추천한 유저수가 변경된 질문 엔티티를 다시 save해서 갱신
	}
	
	public void disvote(Question question, SiteUser siteUser) { //업데이트문으로 만들어줘야함
		question.getDisvoter().add(siteUser);
		//question은 추천을 받은 글의 번호로 조회한 엔티티
		//question의 멤버인 voter를 get해서 voter에 추천을 누른 유저의 엔티티를 추가
		questionRepository.save(question); //추천한 유저수가 변경된 질문 엔티티를 다시 save해서 갱신
	}
	
	public void hit(Question question) { //조회수 증가questionRepository.updateHit(id);
		question.setHit(question.getHit()+1);
		questionRepository.save(question);
	}
	
	private Specification<Question> search(String kw) { //키워드(kw) 검색조회 
		
		return new Specification<Question>() {
			private static final long SerialVersionUID = 1L;
			
			@Override
			public Predicate toPredicate(Root<Question> q, CriteriaQuery<?> query, CriteriaBuilder cb) {
				query.distinct(true); //distinct->중복 제거
				Join<Question, SiteUser> u1 = q.join("author",JoinType.LEFT); //question+siteUser테이블과 left조인
				Join<Question, Answer> a = q.join("answerList",JoinType.LEFT); //question+answer테이블과 조인
				Join<Answer, SiteUser> u2 = a.join("author",JoinType.LEFT);	//answer+siteUser테이블과 left조인
				
				return cb.or(cb.like(q.get("subject"), "%"+kw+"%"), //질문 제목에서 검색어 조회
						cb.like(q.get("content"), "%"+kw+"%"),//질문 내용에서 검색어 조회
						cb.like(u1.get("username"), "%"+kw+"%"),//질문 작성자에서 검색어 조회
						cb.like(a.get("content"), "%"+kw+"%"),//답변 내용에서 검색어 조회
						cb.like(u2.get("username"), "%"+kw+"%")//답변 작성자에서 검색어 조회
						) 
					
						;
			}
		};
		
		
	}
	
}