package gukjin.datajpa.repository;

import gukjin.datajpa.entity.Member;
import gukjin.datajpa.entity.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberJpaRepositoryTest {

    @Autowired private MemberJpaRepository memberJpaRepository;
    @Autowired private TeamRepository teamRepository;

    @Test @DisplayName("일반 JPA 리포지토리 테스트")
    public void findMemberTest(){
        Member member = new Member("memberA");
        Member savedMember = memberJpaRepository.save(member);
        Member findMember = memberJpaRepository.find(savedMember.getId());
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member); //JPA 엔티티 동일성 보장
    }

    @Test
    public void pagingTest(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 10);
        Member m3 = new Member("CCC", 10);
        Member m4 = new Member("DDD", 10);
        Member m5 = new Member("FFF", 10);

        int age = 10;
        int offset = 2; // 몇번부터
        int limit = 3; // 몇개

        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);
        memberJpaRepository.save(m3);
        memberJpaRepository.save(m4);
        memberJpaRepository.save(m5);

        List<Member> members = memberJpaRepository.findAllByPaging(age, offset, limit);
        long totalCount = memberJpaRepository.getTotalCount(age);

        assertThat(members.size()).isEqualTo(3);
        assertThat(totalCount).isEqualTo(5);
    }

    @Test
    public void bulkAgeTest(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 19);
        Member m3 = new Member("CCC", 20);
        Member m4 = new Member("DDD", 25);
        Member m5 = new Member("FFF", 40);

        memberJpaRepository.save(m1);
        memberJpaRepository.save(m2);
        memberJpaRepository.save(m3);
        memberJpaRepository.save(m4);
        memberJpaRepository.save(m5);

        int resultCount = memberJpaRepository.bulkAgePlus(20);
        assertThat(resultCount).isEqualTo(3);
    }

}