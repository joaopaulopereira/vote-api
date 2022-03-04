package com.voteapp.api.service.impl;

import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.toList;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.voteapp.api.enums.StatusSurvey;
import com.voteapp.api.enums.StatusVotation;
import com.voteapp.api.model.Member;
import com.voteapp.api.model.Survey;
import com.voteapp.api.model.Vote;
import com.voteapp.api.model.dto.SurveyDTO;
import com.voteapp.api.model.dto.VoteDTO;
import com.voteapp.api.repository.SurveyRepository;
import com.voteapp.api.service.SurveyService;
import com.voteapp.api.service.exception.InvalidCPFException;
import com.voteapp.api.service.exception.InvalidSessionException;
import com.voteapp.api.service.exception.ObjectNotFoundException;
import com.voteapp.api.service.exception.OneCPFVoteSurveyException;
import com.voteapp.api.service.exception.UnauthorizedCPFException;

@Service
public class SurveyServiceImpl implements SurveyService{
	
	@Autowired
	private SurveyRepository surveyRepository;
	
	private final String API_URL_VALIDATE_CPF = "https://user-info.herokuapp.com/users/";
	
	private final long oneSecondInMillis = 1000L;
	private final Integer oneMinuteDefault = 1;
	private final String SIM="Sim";
	private final String NAO="Não";
	private final String UNABLE_TO_VOTE = "UNABLE_TO_VOTE";

	private final HttpClient client = HttpClient.newBuilder()
			.version(Version.HTTP_2)
			.connectTimeout(Duration.ofSeconds(10))
			.build();
	
	@Override
	public Survey createSurvey(SurveyDTO surveyDTO) {
		Survey newSurvey = new Survey();
		if(surveyDTO != null) {
			newSurvey.setDescription(surveyDTO.getDescription());
		}
		newSurvey.setId(null);
		newSurvey.setStatusSurvey(StatusSurvey.CREATED);
		return surveyRepository.save(newSurvey);
	}
	
	@Override
	public Survey find(Long id) {
		Optional<Survey> survey = surveyRepository.findById(id);
		
		return survey.orElseThrow(() -> new ObjectNotFoundException(
				"Object not found.Id: " + id + ", Class: " + Survey.class.getName()));
	}
	
	@Override
	public void openSession(Survey survey) {
		Survey surveyFromRepo = find(survey.getId());
		surveyFromRepo.setStatusSurvey(StatusSurvey.SESSION_OPENED);
		surveyFromRepo.setMinutes(survey.getMinutes()!=null ? survey.getMinutes() : oneMinuteDefault);
		surveyFromRepo.setFinalTime(LocalDateTime.now().plusMinutes(surveyFromRepo.getMinutes()));
		surveyRepository.save(surveyFromRepo);
	}
	
	@Override
    @Scheduled(fixedRate = oneSecondInMillis)
    public void closeSessionsVotationThisSecond() throws InterruptedException {
    	CompletableFuture.runAsync(() -> {
    		
    		// Creating an object of DateTimeFormatter class
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
          
            // Find Surveys to expire in this second
    		List<Survey> surveysToExpire = surveyRepository.
    				findSurveysToExpireInThisSecond(LocalDateTime.now().withNano(0).format(dtf));
    		
    		surveysToExpire.forEach(survey -> {
    			expireSurvey(survey);
    		});
    	 });
    }

	@Override
    @Transactional
	public void expireSurvey(Survey survey) {
		survey.setStatusSurvey(StatusSurvey.TIMED_OUT);
		survey = surveyRepository.saveAndFlush(survey);
		survey.getVotes();
		countVotes(survey);
	}

	@Override
	public void countVotes(Survey survey) {
		
		Map<Boolean, List<Vote>> groups = 
			      survey.getVotes().stream().collect(partitioningBy(Vote::getContent));

		survey.setCountVotesYes(groups.get(true).size());
		survey.setCountVotesNo(groups.get(false).size());
		
		if(survey.getCountVotesYes()==survey.getCountVotesNo()) {
			survey.setStatusVotation(StatusVotation.TIED);
		}else if(survey.getCountVotesYes()>survey.getCountVotesNo()){
			survey.setStatusVotation(StatusVotation.APPROVED);
		}else {
			survey.setStatusVotation(StatusVotation.NOT_APPROVED);
		}
		
		survey = surveyRepository.saveAndFlush(survey);
	}
	
	
	@Override
	public void processVoteSurvey(VoteDTO vote, Long id, Member memberFromDatabase) throws ExecutionException, TimeoutException, InterruptedException {
		validateCPFInExternalService(vote, id);
	
		Survey survey = find(id);
		
		validateSessionOpened(id, survey);
		
		validateOneVoteSurveyCPF(vote, id, survey);
			
		Vote voteToPersist = new Vote();
		voteToPersist.setContent(extractContentFromDto(vote));
		
		if(memberFromDatabase != null) {
			voteToPersist.setMember(memberFromDatabase);
		}else {
			Member newMember = new Member();
			newMember.setCpf(vote.getCpf());
			voteToPersist.setMember(newMember);
		}
		
		voteToPersist.setSurvey(survey);
		survey.getVotes().add(voteToPersist);
		addVote(survey);
	}

	private void validateOneVoteSurveyCPF(VoteDTO vote, Long id, Survey survey) {
		List<Vote> votesSameCPF = survey.getVotes().stream().filter(v -> v.getMember().getCpf().equals(vote.getCpf())).collect(toList());
		
		if(!votesSameCPF.isEmpty()) {
			throw new OneCPFVoteSurveyException(
					"Only allows one CPF vote in the Survey.Id: " + id + ", Class: " + Survey.class.getName());
		}
	}

	private void validateCPFInExternalService(VoteDTO vote, Long id)
			throws ExecutionException, TimeoutException, InterruptedException {
		if(!validateCPFExternalService(vote.getCpf())) {
			throw new InvalidCPFException(
					"CPF is not valid. Id: " + id + ", Class: " + Survey.class.getName());
		}
	}

	private void validateSessionOpened(Long id, Survey survey) {
		if(!survey.getStatusSurvey().equals(StatusSurvey.SESSION_OPENED)) {
			throw new InvalidSessionException(
					"Survey is not in status session opened. Id: " + id + ", Class: " + Survey.class.getName());
		}
	}
	
	private Boolean extractContentFromDto(VoteDTO vote) {
		switch(vote.getContent()){
		case SIM:
			return true;
		case NAO:
			return false;
		default:
			throw new RuntimeException();
		}
	}
	
	@Override
	public void addVote(Survey survey) {
		surveyRepository.saveAndFlush(survey);
	}

	private boolean validateCPFExternalService(String cpf) throws ExecutionException, TimeoutException, InterruptedException{
		HttpRequest request = HttpRequest.newBuilder()
				  .uri(URI.create(API_URL_VALIDATE_CPF+cpf))
				  .version(HttpClient.Version.HTTP_2)
				  .GET()
				  .build();

		CompletableFuture<HttpResponse<String>> response =
                client.sendAsync(request, HttpResponse.BodyHandlers.ofString());

        String result = response.thenApply(HttpResponse::body).get(5, TimeUnit.SECONDS);
        if(response.get().statusCode()==404) {
        	throw new InvalidCPFException("CPF is invalid. CPF: " + cpf + ", Class: " + Survey.class.getName());
        }
        
        Gson gson = new Gson(); 
        Map<String, Object> map = gson.fromJson(result, new TypeToken<Map<String, Object>>() {}.getType()); 
        String status = (String)map.get("status"); 
        
        if(status.equals(UNABLE_TO_VOTE)) {
        	throw new UnauthorizedCPFException("CPF is unable to vote. CPF: " + cpf + ", Class: " + Survey.class.getName());
        }
        
        return true;
	}
	

}
