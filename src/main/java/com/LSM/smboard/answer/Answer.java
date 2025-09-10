package com.LSM.smboard.answer;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.LSM.smboard.question.Question;
import com.LSM.smboard.user.SiteUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "answer") //실제로 매핑될 데이터베이스의 테이블 이름 설정
@SequenceGenerator(
		name = "ANSWER_SEQ_GENERATOR", //JPA 내부 시퀀스 이름
		sequenceName = "ANSWER_SEQ", //실제 DB 시퀀스 이름 
		initialValue = 1, //시퀀스 시작값
		allocationSize = 1 //시퀀스 증가치
		)
public class Answer {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ANSWER_SEQ_GENERATOR")
	private Integer id; //기본키(자동증가 옵션)
	
	@Column(length = 500)
	private String content; //답변 게시판 내용	
	
	private LocalDateTime createdate; //게시판 답변 등록일시
	
	//N:1 관계 -> 답변들:질문 -> @ManyToOne
	@ManyToOne
	private Question question;
	
	//N:1 관계 -> 답변들:작성자 -> @ManyToOne
		@ManyToOne
		private SiteUser author;
		
		private LocalDateTime modifydate; //질문글 수정 일시
	
	
}