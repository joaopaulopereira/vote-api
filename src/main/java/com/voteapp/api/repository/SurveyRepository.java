package com.voteapp.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.voteapp.api.model.Survey;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
	
	@Query(value = "SELECT S.* FROM SURVEY S WHERE S.STATUS_SURVEY = 1 AND DATE_TRUNC('SECOND', S.FINAL_TIME\\:\\:timestamp) = to_timestamp(:nowWithoutNano, 'YYYY-MM-DD HH24:MI:SS PM')\\:\\:timestamp without time zone", nativeQuery = true)
	List<Survey> findSurveysToExpireInThisSecond(String nowWithoutNano);


}
	