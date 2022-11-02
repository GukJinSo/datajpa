package gukjin.datajpa.controller;

import gukjin.datajpa.dto.MemberDto;
import gukjin.datajpa.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/memberPaging")
    public void memberPaging(){
    }

}
