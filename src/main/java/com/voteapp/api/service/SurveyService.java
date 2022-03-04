package com.voteapp.api.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import com.voteapp.api.model.Member;
import com.voteapp.api.model.Survey;
import com.voteapp.api.model.dto.SurveyDTO;
import com.voteapp.api.model.dto.VoteDTO;

public interface SurveyService {
	
	/** 
	 * Create a Survey from surveyDTO object.
	 * @param surveyDTO
	 * @return
	 */
	Survey createSurvey(SurveyDTO surveyDTO);

	/**
	 * Open survey session for votation
	 * @param survey
	 */
	void openSession(Survey survey);

	/**
	 * Find Survey by id
	 * @param id
	 * @return
	 */
	Survey find(Long id);

	/**
	 * Add vote to Survey
	 * @param survey
	 */
	void addVote(Survey survey);

	/**
	 * Process vote with all business validations before insert into survey.
	 * @param vote
	 * @param id
	 * @param memberFromDatabase
	 * @throws ExecutionException
	 * @throws TimeoutException
	 * @throws InterruptedException
	 */
	void processVoteSurvey(VoteDTO vote, Long id, Member memberFromDatabase) throws ExecutionException, TimeoutException, InterruptedException;

	/**
	 * Close time for vote with async metchod each second
	 * @throws InterruptedException
	 */
	void closeSessionsVotationThisSecond() throws InterruptedException;

	/**
	 * Close time for vote to survey
	 * @param survey
	 */
	void expireSurvey(Survey survey);

	/**
	 * Count votes from a survey
	 * @param survey
	 */
	void countVotes(Survey survey);

	
}	