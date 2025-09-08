package com.LSM.smboard.question;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionForm { //질문 글의 제목과 내용의 유효성체크 클래스

	@NotEmpty(message = "제목은 필수 항목입니다") //공란으로 들어오면 작동
	@Size(max=200) //최대 200글자까지 허용
	@Size(min=5) //최소 5글자 이상만 허용
	private String subject;
	
	@NotEmpty(message = "내용은 필수 항목입니다") //공란으로 들어오면 작동
	@Size(max=500) //최대 500글자까지 허용
	@Size(min=5) //최소 5글자 이상만 허용
	private String content;
}
