package gukjin.datajpa.repository;

import gukjin.datajpa.dto.MemberDto;
import gukjin.datajpa.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {


    List<Member> findByUsernameAndAgeGreaterThan(String username, int age);

    List<Member> findByUsername(@Param("username") String username);

    @Query("select m from Member m where m.username = :username and m.age = :age")
    List<Member> findUser(@Param("username") String username, @Param("age") int age);

    @Query("select m.username from Member m")
    List<String> findUsernameList();

    @Query("select new gukjin.datajpa.dto.MemberDto(m.id, m.username, t.name) from Member m join m.team t")
    List<MemberDto> findMemberDto();

    @Query("select m from Member m where m.username in :names")
    List<Member> findByNames(@Param("names") List<String> names);

    Page<Member> findPagingByAge(int age, Pageable pageRequest);
    @Query(value = "select m from Member m left join m.team t",
            countQuery = "select count(m) from Member m"
    )
    Slice<Member> findSliceByAge(int age, Pageable pageRequest);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update Member m set age = age+1 where age >= :age")
    int bulkAgePlus(@Param("age") int age);

    @EntityGraph(attributePaths = {"team"})
    @Query("select m from Member m left join fetch m.team t")
    List<Member> findEntityGraphByUsername(@Param("username") String username);

    @QueryHints( value = @QueryHint(name = "org.hibernate.readOnly", value = "true"))
    Member findReadOnlyByUsername(String username);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<Member> findLockByUsername(String username);
}
