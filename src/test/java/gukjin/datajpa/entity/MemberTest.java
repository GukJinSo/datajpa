package gukjin.datajpa.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Rollback(value = false)
class MemberTest {

    @PersistenceContext
    EntityManager em;

    @Test @DisplayName("멤버 엔티티 테스트")
    public void memberTest(){

        Team team = new Team("teamA");
        Team team2 = new Team("teamB");

        Member member1 = new Member("haha", 10, team);
        Member member2 = new Member("gg", 20, team);
        Member member3 = new Member("hyhy", 30, team2);
        Member member4 = new Member("hlhl", 40, team2);


        em.persist(team);
        em.persist(team2);
        em.persist(member1);
        em.persist(member2);
        em.persist(member3);
        em.persist(member4);

        em.flush();
        em.clear();

        List<Member> members = em.createQuery("select m from Member m", Member.class).getResultList();
        for (Member member : members) {
            System.out.println("member = " +member);
            System.out.println("team = " +member.getTeam());
        }

    }
}