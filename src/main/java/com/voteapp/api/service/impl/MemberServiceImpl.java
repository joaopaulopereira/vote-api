package com.voteapp.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voteapp.api.model.Member;
import com.voteapp.api.repository.MemberRepository;
import com.voteapp.api.service.MemberService;

@Service
public class MemberServiceImpl implements MemberService {
	
	@Autowired
	private MemberRepository memberRepository;

	@Override
	public Member findByCpf(String cpf) {
		return memberRepository.findByCpf(cpf);
	}
}
