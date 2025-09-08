package com.LSM.smboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LSM.smboard.entity.Question;
import java.util.List;


public interface QuestionRepository extends JpaRepository<Question, Integer>{
	
	public Question findBySubject(String subject);
	
	public Question findBySubjectAndContent(String subject, String content);
	
	public List<Question> findBySubjectLike(String keyword);
}
