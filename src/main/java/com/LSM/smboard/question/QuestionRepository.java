package com.LSM.smboard.question;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	
}
