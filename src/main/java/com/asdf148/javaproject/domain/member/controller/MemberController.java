package com.asdf148.javaproject.domain.member.controller;

import com.asdf148.javaproject.domain.member.payload.ProjectMemberListResponse;
import com.asdf148.javaproject.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequiredArgsConstructor
@RequestMapping("/{projectId}")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member")
    public ProjectMemberListResponse getMemberList(@AuthenticationPrincipal Principal principal, @PathVariable String projectId) {
        return memberService.getMemberList(principal.getName(), projectId);
    }

    @GetMapping("/member/{chatRoomId}")
    public ProjectMemberListResponse getNotParticipatedMemberList(@AuthenticationPrincipal Principal principal, @PathVariable String projectId, @PathVariable String chatRoomId) {
        return memberService.getNotParticipatedMemberList(principal.getName(), projectId, chatRoomId);
    }

}
