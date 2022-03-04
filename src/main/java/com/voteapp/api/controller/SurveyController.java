package com.voteapp.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.voteapp.api.model.Member;
import com.voteapp.api.model.Survey;
import com.voteapp.api.model.Vote;
import com.voteapp.api.model.dto.SurveyDTO;
import com.voteapp.api.model.dto.VoteDTO;
import com.voteapp.api.service.MemberService;
import com.voteapp.api.service.SurveyService;
import com.voteapp.api.service.exception.ExternalServiceException;

@RestController
@RequestMapping("/surveys")
public class SurveyController {
    private static final Logger logger = LogManager.getLogger(SurveyController.class);

	@Autowired
	private SurveyService surveyService;
	
	@Autowired
	private MemberService memberService;
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Survey createSurvey(@RequestBody(required = false) SurveyDTO surveyDTO) {
		return surveyService.createSurvey(surveyDTO);
	}
	
	@PutMapping(value="/{id}/open-session")
	public void openSession(@RequestBody(required = false) Survey survey, @PathVariable Long id) {
		if(survey == null) {
			survey = new Survey();
		}
		survey.setId(id);
		surveyService.openSession(survey);
	}
	
	@GetMapping("{id}")
	public Survey findById(@PathVariable Long id) {
		Survey survey = surveyService.find(id);
		
		Map<Boolean, List<Vote>> groups = 
			      survey.getVotes().stream().collect(Collectors.partitioningBy(v -> v.getContent().equals(Boolean.TRUE)));
		
		List<List<Vote>> subSets = new ArrayList<List<Vote>>(groups.values());
		
		survey.setCountVotesNo(subSets.get(0).size());
		survey.setCountVotesYes(subSets.get(1).size());
		
		return survey;
	}
	
	@PutMapping(value="/{id}/vote")
	public void voteInSurvey(@RequestBody VoteDTO vote, @PathVariable Long id) {
		
		Member memberFromDatabase = memberService.findByCpf(vote.getCpf());
		
		try {
			surveyService.processVoteSurvey(vote, id, memberFromDatabase);
		} catch (ExecutionException | TimeoutException | InterruptedException e) {
			logger.error(e.getMessage(), e);
			throw new ExternalServiceException("External service error. Try again. Class: " + SurveyController.class.getName());
		}
	
	}
	

}