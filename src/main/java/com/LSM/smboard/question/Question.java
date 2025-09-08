package com.LSM.smboard.question;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;

import com.LSM.smboard.answer.Answer;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity //DB테이블과 매핑활 entity클래스로 설정
@Table(name = "question") //실제로 매핑될 데이터베이스의 테이블 이름 설정
@SequenceGenerator(
		name = "QUESTION_SEQ_GENERATOR",//JPA 내부 시퀀스 이름
		sequenceName = "QUESTION_SEQ", //실제 DB 시퀀스 이름
		initialValue = 1, //시퀀스 시작값
		allocationSize = 1 //시퀀스 증가치
		)
public class Question {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "QUESTION_SEQ_GENERATOR")
	private Integer id; //질문 게시판의 글번호(기본키-자동증가)
	
	@Column(length = 200) //질문 게시판 제목은 200자까지 가능
	private String subject; //질문 게시판의 제목
	
	@Column(length = 500)
	private String content; //질문 게시판의 내용
	
	@CreatedDate
	private LocalDateTime createdate;
	
	//1:N 관계 ->질문:답변들 ->@one To many
	@OneToMany(mappedBy = "question", cascade = CascadeType.REMOVE) //Answer에서 적은 private Question question;
	//cascade ->질문글(부모글)이 삭제될 경우 답변들(자식글)이 함께 삭제되게 하는 설정
	private List<Answer> answerList;
}
