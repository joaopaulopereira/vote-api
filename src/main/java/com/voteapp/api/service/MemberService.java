package com.voteapp.api.service;

import com.voteapp.api.model.Member;

public interface MemberService {

	
	/**
	 * Find Member by CPF
	 * @param cpf
	 * @return
	 */
	Member findByCpf(String cpf);

}
