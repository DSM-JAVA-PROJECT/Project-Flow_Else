package com.asdf148.javaproject.domain.member.controller;

import com.asdf148.javaproject.domain.member.payload.ProjectMemberListResponse;
import com.asdf148.javaproject.domain.member.service.MemberService;
import com.asdf148.javaproject.global.config.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{projectId}")
public class MemberController {

    private final MemberService memberService;
    private final JwtUtil jwtUtil;

    @GetMapping("/member")
    public ProjectMemberListResponse getMemberList(@RequestHeader("Authorization") String token, @PathVariable String projectId) {
        return memberService.getMemberList(jwtUtil.decodeToken(token.substring(7)).getEmail(), projectId);
    }

    @GetMapping("/member/{chatRoomId}")
    public ProjectMemberListResponse getNotParticipatedMemberList(@RequestHeader("Authorization") String token, @PathVariable String projectId, @PathVariable String chatRoomId) {
        return memberService.getNotParticipatedMemberList(jwtUtil.decodeToken(token.substring(7)).getEmail(), projectId, chatRoomId);
    }

}
