package com.ec2test.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.ec2test.dto.MemberRequestDto;
import com.ec2test.dto.MemberResponseDto;
import com.ec2test.entity.Member;
import com.ec2test.exception.MemberNotFoundException;
import com.ec2test.repository.MemberRepository;
import java.lang.reflect.Field;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private S3Service s3Service;

    @Test
    @DisplayName("팀원 저장 서비스는 팀원 정보를 저장한다")
    void createMember() throws Exception {
        MemberRequestDto requestDto = new MemberRequestDto();
        setField(requestDto, "name", "Eungi");
        setField(requestDto, "age", 27);
        setField(requestDto, "mbti", "INTJ");

        Member savedMember = Member.builder()
                .name("Eungi")
                .age(27)
                .mbti("INTJ")
                .build();
        setField(savedMember, "id", 1L);

        when(memberRepository.save(any(Member.class))).thenReturn(savedMember);

        MemberResponseDto responseDto = memberService.createMember(requestDto);

        assertEquals(1L, responseDto.getId());
        assertEquals("Eungi", responseDto.getName());
        assertEquals(27, responseDto.getAge());
        assertEquals("INTJ", responseDto.getMbti());
    }

    @Test
    @DisplayName("없는 팀원 조회 시 예외를 던진다")
    void getMemberNotFound() {
        when(memberRepository.findById(9999L)).thenReturn(Optional.empty());

        assertThrows(MemberNotFoundException.class, () -> memberService.getMember(9999L));
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
