package com.workflow.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workflow.entity.ApprovalStep;
import com.workflow.entity.Request;
import com.workflow.entity.enums.RequestType;

public interface ApprovalStepRepo extends JpaRepository<ApprovalStep, Long> {

	Optional<ApprovalStep> findByRequestTypeAndStepOrder(RequestType request, Integer currentStep);
	
	List<ApprovalStep> findByRequestTypeOrderByStepOrder(RequestType request);

}
