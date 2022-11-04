package gukjin.datajpa.repository;

import gukjin.datajpa.dto.MemberDto;
import gukjin.datajpa.entity.Member;
import gukjin.datajpa.entity.Team;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {

    @PersistenceContext
    EntityManager em;
    @Autowired MemberRepository memberRepository;
    @Autowired TeamRepository teamRepository;

    @Test @DisplayName("SPRING DATA JPA 리포지토리 테스트")
    public void testMember() {

        System.out.println("MemberRepository class = " +memberRepository.getClass());

        Member member = new Member("memberA");
        Member savedMember = memberRepository.save(member);
        Member findMember =
                memberRepository.findById(savedMember.getId()).get();
        assertThat(findMember.getId()).isEqualTo(member.getId());
        assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(findMember).isEqualTo(member); //JPA 엔티티 동일성 보장
    }

    @Test
    public void basicCRUD() {
        Member member1 = new Member("member1");
        Member member2 = new Member("member2");
        memberRepository.save(member1);
        memberRepository.save(member2);

        //단건 조회 검증
        Member findMember1 =
                memberRepository.findById(member1.getId()).get();
        Member findMember2 =
                memberRepository.findById(member2.getId()).get();
        assertThat(findMember1).isEqualTo(member1);
        assertThat(findMember2).isEqualTo(member2);
        //리스트 조회 검증
        List<Member> all = memberRepository.findAll();
        assertThat(all.size()).isEqualTo(2);
        //카운트 검증
        long count = memberRepository.count();
        assertThat(count).isEqualTo(2);
        //삭제 검증
        memberRepository.delete(member1);
        memberRepository.delete(member2);
        long deletedCount = memberRepository.count();
        assertThat(deletedCount).isEqualTo(0);
    }

    @Test
    public void testQuery() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> result = memberRepository.findUser("AAA", 10);
        assertThat(result.get(0)).isEqualTo(m1);
    }

    @Test
    public void findUsernameList() {
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 20);
        memberRepository.save(m1);
        memberRepository.save(m2);

        List<String> usernameList = memberRepository.findUsernameList();
        System.out.println(usernameList.size());
    }

    @Test
    public void findUserWithTeamDto() {

        Team team = new Team("아스날");
        teamRepository.save(team);

        Member m1 = new Member("AAA", 10);
        m1.setTeam(team);
        memberRepository.save(m1);

        List<MemberDto> memberDtos = memberRepository.findMemberDto();
        for (MemberDto memberDto : memberDtos) {
            System.out.println("dto = " + memberDto);
        }
    }

    @Test
    public void findByNames() {

        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 10);

        memberRepository.save(m1);
        memberRepository.save(m2);

        List<Member> members = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
        for (Member member : members) {
            System.out.println("member = " + member);
        }
    }
    @Test
    public void pagingTest(){

        Team t1 = new Team("아스날");
        Team t2 = new Team("리버풀");
        Team t3 = new Team("첼시");

        teamRepository.save(t1);
        teamRepository.save(t2);
        teamRepository.save(t3);
        Member m1 = new Member("AAA", 10);
        m1.setTeam(t1);
        Member m2 = new Member("BBB", 10);
        m2.setTeam(t1);
        Member m3 = new Member("CCC", 10);
        m3.setTeam(t2);
        Member m4 = new Member("DDD", 10);
        m4.setTeam(t3);
        Member m5 = new Member("FFF", 10);

        int age = 10;
        int offset = 2; // 몇번부터
        int limit = 3; // 몇개

        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);
        memberRepository.save(m4);
        memberRepository.save(m5);

        PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "username"));

        Slice<Member> page = memberRepository.findSliceByAge(age, pageRequest);

        for (Member member : page) {
            System.out.println(member.getTeam());
        }

        Slice<MemberDto> map = page.map(member -> new MemberDto(member.getId(), member.getUsername(), member.getTeam() == null? null : member.getTeam().getName()));
        System.out.println(map);

        List<Member> content = page.getContent();

        assertThat(content.size()).isEqualTo(3);
        assertThat(page.getNumber()).isEqualTo(0);
        assertThat(page.isFirst());
        assertThat(page.hasNext());

    }

    @Test
    public void bulkAgePlusTest(){
        Member m1 = new Member("AAA", 10);
        Member m2 = new Member("BBB", 19);
        Member m3 = new Member("CCC", 20);
        Member m4 = new Member("DDD", 25);
        Member m5 = new Member("FFF", 40);

        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);
        memberRepository.save(m4);
        memberRepository.save(m5);

        int resultCount = memberRepository.bulkAgePlus(20);
        assertThat(resultCount).isEqualTo(3);

        em.flush();
        em.clear();

        List<Member> fff = memberRepository.findByUsername("FFF");
        System.out.println("FFF age = " + fff.get(0).getAge());

    }

    @Test
    public void findMemberLazy(){

        Team t1 = new Team("아스날");
        Team t2 = new Team("리버풀");
        Team t3 = new Team("첼시");

        teamRepository.save(t1);
        teamRepository.save(t2);
        teamRepository.save(t3);

        Member m1 = new Member("FFF", 40);
        m1.setTeam(t1);
        Member m2 = new Member("FFF", 40);
        m2.setTeam(t1);
        Member m3 = new Member("FFF", 40);
        m3.setTeam(t2);
        Member m4 = new Member("FFF", 40);
        m4.setTeam(t3);
        Member m5 = new Member("FFF", 40);

        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);
        memberRepository.save(m4);
        memberRepository.save(m5);

        List<Member> members = memberRepository.findEntityGraphByUsername("FFF");
        for (Member member : members) {
            System.out.println(member.getTeam() != null? member.getTeam().getName() : "");
        }
    }

    @Test
    public void testCustomRepo(){

        Member m1 = new Member("FFF", 40);
        Member m2 = new Member("FFF", 40);
        Member m3 = new Member("FFF", 40);
        Member m4 = new Member("FFF", 40);
        Member m5 = new Member("FFF", 40);

        memberRepository.save(m1);
        memberRepository.save(m2);
        memberRepository.save(m3);
        memberRepository.save(m4);
        memberRepository.save(m5);

        List<Member> members = memberRepository.findMemberCustom();
        for (Member member : members) {
            System.out.println(member);
        }
    }


    @Test
    public void QueryByExample(){
        Team team = new Team("아스날");
        em.persist(team);
        em.persist(new Member("GJ S", 0,team));
        em.persist(new Member("GK S", 0,team));
        em.flush();

        // Probe: 필드에 데이터가 있는 실제 도메인 객체
        Member memberExample = new Member("GJ S");
        Team teamExample = new Team("아스날");
        memberExample.setTeam(teamExample);

        // ExampleMatcher: 특정 필드를 일치시키는 상세한 정보 제공, 재사용 가능
        ExampleMatcher matcher = ExampleMatcher.matching().withIgnorePaths("age");

        // Example: Probe와 ExampleMatcher로 구성, 쿼리를 생성하는데 사용
        Example<Member> example = Example.of(memberExample, matcher);

        List<Member> result = memberRepository.findAll(example);

        Assertions.assertThat(result.size()).isEqualTo(1);
    }

    @Test
    public void projection() throws Exception {
        Team team = new Team("아스날");
        em.persist(team);
        em.persist(new Member("GJ S", 0,team));
        em.persist(new Member("GJ S", 0,team));
        em.flush();

        List<NestedProjection> result = memberRepository.findProjectionByUsername("GJ S", NestedProjection.class);

        for (NestedProjection userAndTeam : result) {
            System.out.println("username = " + userAndTeam.getUsername());
            System.out.println("teamName = " + userAndTeam.getTeam().getName());
        }

    }
        @Test
    public void nativeQuery() throws Exception {
        Team team = new Team("아스날");
        em.persist(team);
        em.persist(new Member("GJ S", 0,team));
        em.persist(new Member("GJ S", 0,team));
        em.flush();

        Page<MemberProjection> byNativeQueryPaging = memberRepository.findByNativeQueryPaging(PageRequest.of(0, 10));

        for (MemberProjection members : byNativeQueryPaging) {
            System.out.println("members.getId() = " + members.getId());
            System.out.println("username = " + members.getUsername());
            System.out.println("teamName = " + members.getTeamName());
        }

    }

    

}