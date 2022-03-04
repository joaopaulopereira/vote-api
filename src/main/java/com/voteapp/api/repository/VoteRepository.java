package com.voteapp.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.voteapp.api.model.Vote;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

}
