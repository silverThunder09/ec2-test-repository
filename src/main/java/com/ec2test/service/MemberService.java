package com.ec2test.service;

import com.ec2test.dto.MemberRequestDto;
import com.ec2test.dto.MemberResponseDto;
import com.ec2test.dto.ProfileImageUploadResponseDto;
import com.ec2test.dto.ProfileImageUrlResponseDto;
import com.ec2test.entity.Member;
import com.ec2test.exception.MemberNotFoundException;
import com.ec2test.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final S3Service s3Service;

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
    public MemberResponseDto getMember(Long memberId) {

        Member member = findByMemberId(memberId);

        return MemberResponseDto.of(member);
    }

    @Transactional
    public ProfileImageUploadResponseDto uploadProfileImage(Long memberId, MultipartFile file) {

        Member member = findByMemberId(memberId);

        String profileImageKey = s3Service.uploadProfileImage(memberId, file);
        member.updateProfileImageKey(profileImageKey);

        return new ProfileImageUploadResponseDto(member.getId(), profileImageKey);
    }

    @Transactional(readOnly = true)
    public ProfileImageUrlResponseDto getProfileImageUrl(Long memberId) {

        Member member = findByMemberId(memberId);

        if (member.getProfileImageKey() == null) {
            throw new IllegalArgumentException("등록된 프로필 이미지가 없습니다.");
        }

        S3Service.PresignedUrlResult result = s3Service.createProfileImageDownloadUrl(member.getProfileImageKey());

        return new ProfileImageUrlResponseDto(
                member.getId(),
                result.getUrl(),
                result.getExpiresAt()
        );
    }

    public Member findByMemberId(Long id) {
       return memberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException(id));
    }
}
