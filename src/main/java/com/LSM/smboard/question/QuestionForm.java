package com.LSM.smboard.question;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionForm { //질문 글의 제목과 내용의 유효성체크 클래스

	
	@NotEmpty(message = "제목은 필수 항목입니다") //공란으로 들어오면 작동
	@Size(max=200,message="제목은 200자 이하로 작성가능합니다") //최대 200글자까지 허용
	@Size(min=5,message="제목은 5자 이상 작성하셔야 합니다") //최소 5글자 이상만 허용
	private String subject;
	
	
	@NotEmpty(message = "내용은 필수 항목입니다") //공란으로 들어오면 작동
	@Size(max=500,message="내용은 500자 이하로 작성가능합니다") //최대 500글자까지 허용
	@Size(min=5,message="내용은 5자 이상 작성하셔야 합니다") //최소 5글자 이상만 허용
	private String content;
}
