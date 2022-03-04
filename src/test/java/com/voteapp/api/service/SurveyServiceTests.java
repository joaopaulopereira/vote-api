package com.voteapp.api.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.voteapp.api.enums.StatusSurvey;
import com.voteapp.api.enums.StatusVotation;
import com.voteapp.api.model.Survey;
import com.voteapp.api.model.Vote;
import com.voteapp.api.model.dto.SurveyDTO;
import com.voteapp.api.repository.SurveyRepository;
import com.voteapp.api.service.exception.ObjectNotFoundException;

@SpringBootTest
public class SurveyServiceTests {

	@Autowired
	private SurveyService service;
	
	@MockBean
	private SurveyRepository repository;
	
	@MockBean
	private HttpClient client;
	
	@Test
	public void test_create_survey_null_description() {
		Survey surveyReference = new Survey();
		surveyReference.setStatusSurvey(StatusSurvey.CREATED);
		
		Survey surveyReturned = new Survey();
		surveyReturned.setId(11l);
		surveyReturned.setStatusSurvey(StatusSurvey.SESSION_OPENED);
		
		Mockito.when(repository.save(any())).thenReturn(surveyReturned);
		
		Survey surveyCreated = service.createSurvey(null);
		
		Mockito.verify(repository).save(surveyReference);
		
		Assertions.assertEquals(null,surveyCreated.getDescription());
		Assertions.assertEquals(11l,surveyCreated.getId());
		Assertions.assertEquals(StatusSurvey.SESSION_OPENED,surveyCreated.getStatusSurvey());
	}
	
	@Test
	public void test_create_survey_with_description() {
		SurveyDTO surveyDTO = new SurveyDTO();
		surveyDTO.setDescription("Question01");
		
		Survey surveyInitial = new Survey();
		surveyInitial.setDescription("Question01");
		
		Mockito.when(repository.save(any())).thenReturn(surveyInitial);
		
		Survey returnedSurvey = service.createSurvey(surveyDTO);
		Assertions.assertEquals(surveyDTO.getDescription(), returnedSurvey.getDescription());
	}
	
	@Test
	public void test_open_session_with_survey_invalid_id() {
		Survey survey = new Survey();
		survey.setId(1L);
		
		ObjectNotFoundException thrown = Assertions.assertThrows(ObjectNotFoundException.class, ()-> service.openSession(survey));
		assertTrue(thrown.getMessage().contains("Object not found"));
	}
	
	@Test
	public void test_find_with_valid_id(){
		
		Survey survey = new Survey();
		survey.setId(1l);
		
		Mockito.when(repository.findById(1L)).thenReturn(Optional.of(survey));
		
		Survey survey1 =repository.findById(1l).get();
		
		assertEquals(1l, survey1.getId());
	}
	
	@Test
	public void test_open_session_with_valid_survey() {
		Survey survey = new Survey();
		survey.setId(12l);
		
		Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(survey));
		
		service.openSession(survey);
		
		assertEquals(StatusSurvey.SESSION_OPENED, survey.getStatusSurvey());
	}
	
	@Test
	public void test_open_session_with_custom_minutes() {
		Survey survey = new Survey();
		survey.setId(12l);
		survey.setMinutes(15);
		
		Mockito.when(repository.findById(Mockito.anyLong()))
			.thenReturn(Optional.of(survey));
		
		service.openSession(survey);
		
		assertEquals(15, survey.getMinutes());
	}
	
	@Test
	public void test_expire_valid_survey() throws InterruptedException {
		Survey survey = new Survey();
		survey.setStatusSurvey(StatusSurvey.TIMED_OUT);
		survey.setVotes(new ArrayList<>());
		
		when(repository.saveAndFlush(any())).thenReturn(survey);
		
		service.expireSurvey(survey);
		
		verify(repository, times(2)).saveAndFlush(survey);
		
		assertEquals(StatusSurvey.TIMED_OUT, survey.getStatusSurvey());
	}
	
	@Test
	public void test_count_votes_1_yes() {
		
		Survey survey = new Survey();
		survey.setStatusSurvey(StatusSurvey.TIMED_OUT);
		survey.setVotes(new ArrayList<>());

		Vote voteYes = new Vote();
		voteYes.setContent(true);
		voteYes.setSurvey(survey);
		
		survey.getVotes().add(voteYes);
		
		service.countVotes(survey);
	
		verify(repository).saveAndFlush(survey);
		
		assertEquals(StatusVotation.APPROVED, survey.getStatusVotation());
		
	}
	
	@Test
	public void test_count_votes_1_no() {
		
		Survey survey = new Survey();
		survey.setStatusSurvey(StatusSurvey.TIMED_OUT);
		survey.setVotes(new ArrayList<>());

		Vote voteNo = new Vote();
		voteNo.setContent(false);
		voteNo.setSurvey(survey);
		
		survey.getVotes().add(voteNo);
		
		service.countVotes(survey);
	
		verify(repository).saveAndFlush(survey);
		
		assertEquals(StatusVotation.NOT_APPROVED, survey.getStatusVotation());
		
	}
	
	@Test
	public void test_count_votes_tied() {
		
		Survey survey = new Survey();
		survey.setStatusSurvey(StatusSurvey.TIMED_OUT);
		survey.setVotes(new ArrayList<>());

		Vote voteNo = new Vote();
		voteNo.setContent(false);
		voteNo.setSurvey(survey);
		
		Vote voteYes = new Vote();
		voteYes.setContent(true);
		voteYes.setSurvey(survey);
		
		survey.getVotes().add(voteNo);
		survey.getVotes().add(voteYes);
		
		service.countVotes(survey);
	
		verify(repository).saveAndFlush(survey);
		
		assertEquals(StatusVotation.TIED, survey.getStatusVotation());
		
	}
	
	
	
}
