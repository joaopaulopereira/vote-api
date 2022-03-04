package com.voteapp.api.model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.voteapp.api.enums.StatusSurvey;
import com.voteapp.api.enums.StatusVotation;

@Entity
public class Survey {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_survey")
	private Long id; 

	@Column(length = 250)
	private String description;
	
	@Column
	private StatusVotation statusVotation;
	
	@Column
	private StatusSurvey statusSurvey;
	
	@Column
	private Integer minutes;
	
	@Column
	private LocalDateTime finalTime;

	@OneToMany(fetch = FetchType.LAZY,  mappedBy = "survey", cascade = CascadeType.ALL)
	private List<Vote> votes;
	
	@Transient
	private Integer countVotesYes;

	@Transient
	private Integer countVotesNo;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	public Integer getMinutes() {
		return minutes;
	}

	public void setMinutes(Integer minutes) {
		this.minutes = minutes;
	}

	public LocalDateTime getFinalTime() {
		return finalTime;
	}

	public void setFinalTime(LocalDateTime finalTime) {
		this.finalTime = finalTime;
	}

	public List<Vote> getVotes() {
		return votes;
	}

	public void setVotes(List<Vote> votes) {
		this.votes = votes;
	}

	public StatusSurvey getStatusSurvey() {
		return statusSurvey;
	}

	public void setStatusSurvey(StatusSurvey statusSurvey) {
		this.statusSurvey = statusSurvey;
	}

	public Integer getCountVotesYes() {
		return countVotesYes;
	}

	public void setCountVotesYes(Integer countVotesYes) {
		this.countVotesYes = countVotesYes;
	}

	public Integer getCountVotesNo() {
		return countVotesNo;
	}

	public void setCountVotesNo(Integer countVotesNo) {
		this.countVotesNo = countVotesNo;
	}
	
	public StatusVotation getStatusVotation() {
		return statusVotation;
	}

	public void setStatusVotation(StatusVotation statusVotation) {
		this.statusVotation = statusVotation;
	}

	@Override
	public int hashCode() {
		return Objects.hash(countVotesNo, countVotesYes, description, finalTime, id, minutes, statusSurvey, votes);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Survey other = (Survey) obj;
		return Objects.equals(countVotesNo, other.countVotesNo) && Objects.equals(countVotesYes, other.countVotesYes)
				&& Objects.equals(description, other.description) && Objects.equals(finalTime, other.finalTime)
				&& Objects.equals(id, other.id) && Objects.equals(minutes, other.minutes) && statusSurvey == other.statusSurvey
				&& Objects.equals(votes, other.votes);
	}

	
	
}
