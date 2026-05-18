package com.ec2test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.ec2test.dto.MemberRequestDto;
import com.ec2test.dto.MemberResponseDto;
import com.ec2test.exception.MemberNotFoundException;
import java.lang.reflect.Field;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    @DisplayName("팀원 저장 서비스는 팀원 정보를 저장한다")
    void createMember() throws Exception {
        MemberRequestDto requestDto = new MemberRequestDto();
        setField(requestDto, "name", "Eungi");
        setField(requestDto, "age", 27);
        setField(requestDto, "mbti", "INTJ");

        MemberResponseDto responseDto = memberService.createMember(requestDto);

        assertEquals("Eungi", responseDto.getName());
        assertEquals(27, responseDto.getAge());
        assertEquals("INTJ", responseDto.getMbti());
    }

    @Test
    @DisplayName("없는 팀원 조회 시 예외를 던진다")
    void getMemberNotFound() {
        assertThrows(MemberNotFoundException.class, () -> memberService.getMember(9999L));
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
