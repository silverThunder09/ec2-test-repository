package com.ec2test.service;

import com.ec2test.dto.MemberRequestDto;
import com.ec2test.dto.MemberResponseDto;
import com.ec2test.entity.Member;
import com.ec2test.exception.MemberNotFoundException;
import com.ec2test.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public MemberResponseDto createMember(MemberRequestDto requestDto) {

        Member member = Member.builder()
                .name(requestDto.getName())
                .age(requestDto.getAge())
                .mbti(requestDto.getMbti())
                .build();

        Member saveMember = memberRepository.save(member);

        return MemberResponseDto.of(saveMember);
    }

    @Transactional(readOnly = true)
    public MemberResponseDto getMember(Long id) {

        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));

        return MemberResponseDto.of(member);
    }
}
