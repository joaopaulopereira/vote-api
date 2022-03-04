package com.voteapp.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.voteapp.api.model.Member;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

	Member findByCpf(String cpf);
}
