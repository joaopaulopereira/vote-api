package com.voteapp.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.voteapp.api.model.Vote;
import com.voteapp.api.repository.VoteRepository;
import com.voteapp.api.service.VoteService;

@Service
public class VoteServiceImpl implements VoteService {
	
	@Autowired
	private VoteRepository voteRepository;

	@Override
	public Vote register(Vote vote) {
		return voteRepository.save(vote);
	}

}
