package gukjin.datajpa.controller;

import gukjin.datajpa.dto.MemberDto;
import gukjin.datajpa.entity.Member;
import gukjin.datajpa.entity.Team;
import gukjin.datajpa.repository.MemberRepository;
import gukjin.datajpa.repository.TeamRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Request;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final TeamRepository teamRepository;


    @GetMapping("member/v1/{id}")
    public String MemberV1(@PathVariable("id") Long id){
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }

    @GetMapping("member/v2/{id}")
    public String MemberV2(@PathVariable("id") Member member){
        return member.getUsername();
    }

    @AllArgsConstructor @Getter
    static class CustomPaging{
        private List<MemberDto> list;
        private int size;
        private int totalPage;
        private int currentPage;
    }

    @GetMapping("membersV1")
    public Page<MemberDto> membersV1(Pageable pageable){
        return memberRepository.findAll(pageable).map((m) -> new MemberDto(m.getId(), m.getUsername(), null));
    }

    @GetMapping("membersV2")
    public CustomPaging membersV2(Pageable pageable){
        System.out.println("getPageNumber = "+pageable.getPageNumber());
        Page<Member> pagingOrigin = memberRepository.findAll(pageable);
        List<MemberDto> map = pagingOrigin.map((m) -> new MemberDto(m.getId(), m.getUsername(), null)).stream().collect(Collectors.toList());
        return new CustomPaging(map, pagingOrigin.getSize(), pagingOrigin.getTotalPages(), pagingOrigin.getNumber()+1);
    }

    @PostConstruct
    public void init(){
        for(int i=0; i < 100; i++){
            memberRepository.save(new Member("Gukjin" +i, 20+i));
        }
    }

}
