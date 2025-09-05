package com.LSM.smboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.LSM.smboard.entity.Question;

public interface QuestionRepository extends JpaRepository<Question, Integer>{
	
}
