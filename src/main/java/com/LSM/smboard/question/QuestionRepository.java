package com.LSM.smboard.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

import java.util.List;


public interface QuestionRepository extends JpaRepository<Question, Integer>{
	
	public Question findBySubject(String subject);
	
	public Question findBySubjectAndContent(String subject, String content);
	
	public List<Question> findBySubjectLike(String keyword);
	
	//페이징 관련
	//public Page<Question> findAll(Pageable pageable);
	@Query(
	         value = "SELECT * FROM ( " +
	                 " SELECT q.*, ROWNUM rnum FROM ( " +
	                 "   SELECT * FROM question ORDER BY CREATEDATE DESC " +
	                 " ) q WHERE ROWNUM <= :endRow " +
	                 ") WHERE rnum > :startRow",
	         nativeQuery = true)
	    List<Question> findQuestionsWithPaging(@Param("startRow") int startRow,
	                                           @Param("endRow") int endRow);
	//검색을 조회하는 페이징
	@Query(value = 
		       "SELECT * FROM ( " +
		       "   SELECT q.*, ROWNUM rnum FROM ( " +
		       "       SELECT DISTINCT q.* " +
		       "       FROM question q " +
		       "       LEFT OUTER JOIN siteuser u1 ON q.author_id = u1.id " +
		       "       LEFT OUTER JOIN answer a ON a.question_id = q.id " +
		       "       LEFT OUTER JOIN siteuser u2 ON a.author_id = u2.id " +
		       "       WHERE q.subject LIKE '%'||:kw||'%' " +
		       "          OR q.content LIKE '%'||:kw||'%'" +
		       "          OR u1.username LIKE '%'||:kw||'%'" +
		       "          OR a.content LIKE '%'||:kw||'%'" +
		       "          OR u2.username LIKE '%'||:kw||'%'" +
		       "       ORDER BY q.createdate DESC " +
		       "   ) q WHERE ROWNUM <= :endRow " +
		       ") WHERE rnum > :startRow", 
		       nativeQuery = true)
	 List<Question> searchQuestionsWithPaging(@Param("kw") String kw,@Param("startRow") int startRow,
             @Param("endRow") int endRow);
	//검색 결과 총 갯수 반환
	@Query(value = 
  
    "   SELECT COUNT(DISTINCT q.id) " +
    "       FROM question q " +
    "       LEFT OUTER JOIN siteuser u1 ON q.author_id = u1.id " +
    "       LEFT OUTER JOIN answer a ON a.question_id = q.id " +
    "       LEFT OUTER JOIN siteuser u2 ON a.author_id = u2.id " +
    "       WHERE q.subject LIKE '%'||:kw||'%' " +
    "          OR q.content LIKE '%'||:kw||'%'" +
    "          OR u1.username LIKE '%'||:kw||'%'" +
    "          OR a.content LIKE '%'||:kw||'%'" +
    "          OR u2.username LIKE '%'||:kw||'%'",
           nativeQuery = true)
	public int countSearchResult(@Param("kw") String kw);
	
	//Page<Question> findAll(Specification<Question> spec, Pageable pageable);
	
}
