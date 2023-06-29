package com.kakaobean.core.member.domain.repository;

import com.kakaobean.core.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    @Query("select m from Member m where m.auth.email = :email and m.status = 'ACTIVE'")
    Optional<Member> findMemberByEmail(String email);

    @Query("select m from Member m where m.id = :id and m.status = 'ACTIVE'")
    Optional<Member> findMemberById(Long id);
}