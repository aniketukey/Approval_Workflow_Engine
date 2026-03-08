package com.workflow.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import com.workflow.dto.RequestDetailsDto;
import com.workflow.entity.ApprovalHistory;
import com.workflow.entity.ApprovalStep;
import com.workflow.entity.Request;
import com.workflow.entity.enums.RequestStatus;
import com.workflow.entity.enums.RequestType;
import com.workflow.repository.ApprovalHistoryRepo;
import com.workflow.repository.ApprovalStepRepo;
import com.workflow.repository.RequestRepo;
import com.workflow.repository.UserRepo;
import com.workflow.entity.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

	private final RequestRepo requestRepo;
	private final ApprovalHistoryRepo approvalHistoryRepo;
	private final ApprovalStepRepo approvalStepRepo;
	private final UserRepo userRepo;

	private User getAuthenticatedUser() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username;
		if (principal instanceof UserDetails) {
			username = ((UserDetails) principal).getUsername();
		} else {
			username = principal.toString();
		}

		return userRepo.findByEmail(username)
				.orElseThrow(() -> new RuntimeException("Authenticated user not found"));
	}

	@Override
	public Request createRequest(String type) {
		User user = getAuthenticatedUser();
		Long userId = user.getId();

		RequestType requestType;
		try {
			requestType = RequestType.valueOf(type.trim().toUpperCase());
		} catch (IllegalArgumentException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid request type: " + type);
		}

		Request request = new Request();

		request.setRequestType(requestType);
		request.setStatus(RequestStatus.PENDING);
		request.setCreatedBy(userId);
		request.setCreatedAt(LocalDateTime.now());
		request.setCurrentStep(1);

		Request saved = requestRepo.save(request);

		ApprovalHistory history = new ApprovalHistory();
		history.setRequestId(saved.getId());
		history.setAction("CREATED");
		history.setActionBy(userId);
		history.setActionAt(LocalDateTime.now());

		approvalHistoryRepo.save(history);

		return saved;
	}

	@Override
	public RequestDetailsDto getRequestDetails(Long reqId) {

		Request request = requestRepo.findById(reqId)
				.orElseThrow(
						() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found with id: " + reqId));

		Optional<ApprovalStep> step = approvalStepRepo.findByRequestTypeAndStepOrder(request.getRequestType(),
				request.getCurrentStep());

		String role = null;
		if (step.isPresent() && step.get().getRole() != null) {
			role = step.get().getRole().name();
		} else if (request.getStatus() != RequestStatus.PENDING) {
			role = "WORKFLOW_COMPLETED";
		}

		RequestDetailsDto response = new RequestDetailsDto();

		response.setRequestId(request.getId());
		response.setRequestType(request.getRequestType().toString());
		response.setStatus(request.getStatus().name());
		response.setCreatedBy(request.getCreatedBy());
		response.setCreatedAt(request.getCreatedAt());
		response.setCurrentStep(request.getCurrentStep());
		response.setCurrentApproverRole(role);

		return response;
	}

}
