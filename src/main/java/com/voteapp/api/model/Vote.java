package com.voteapp.api.model;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Vote {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="id_vote")
	private Long id;

	private Boolean content;
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "id_member")    
	private Member member;
	
	@ManyToOne
	@JoinColumn(name = "id_survey")
	@JsonIgnore
	private Survey survey;

	public Boolean getContent() {
		return content;
	}

	public void setContent(Boolean content) {
		this.content = content;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public int hashCode() {
		return Objects.hash(content, id, member);
	}

	
	public Survey getSurvey() {
		return survey;
	}

	public void setSurvey(Survey survey) {
		this.survey = survey;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Vote other = (Vote) obj;
		return Objects.equals(content, other.content) && Objects.equals(id, other.id)
				&& Objects.equals(member, other.member);
	}
	
	
	
}
