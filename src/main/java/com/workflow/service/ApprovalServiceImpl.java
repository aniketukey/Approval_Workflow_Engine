package com.workflow.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.workflow.entity.ApprovalHistory;
import com.workflow.entity.ApprovalStep;
import com.workflow.entity.Request;
import com.workflow.entity.User;
import com.workflow.entity.enums.RequestStatus;
import com.workflow.repository.ApprovalHistoryRepo;
import com.workflow.repository.ApprovalStepRepo;
import com.workflow.repository.RequestRepo;
import com.workflow.repository.UserRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ApprovalServiceImpl implements ApprovalService {

    private final RequestRepo requestRepo;
    private final ApprovalStepRepo approvalStepRepo;
    private final ApprovalHistoryRepo approvalHistoryRepo;
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
    public void approve(Long reqId) {
        User user = getAuthenticatedUser();
        Request request = requestRepo.findById(reqId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));

        if (request.getCreatedBy().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Requester cannot approve own request");
        }

        Optional<ApprovalStep> currentStep = approvalStepRepo.findByRequestTypeAndStepOrder(
                request.getRequestType(), request.getCurrentStep());

        if (currentStep.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Workflow step not defined for this request type and step order");
        }

        if (!currentStep.get().getRole().equals(user.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Role mismatch: This step requires "
                    + currentStep.get().getRole() + " but you are " + user.getRole());
        }

        request.setCurrentStep(request.getCurrentStep() + 1);

        Optional<ApprovalStep> nextStep = approvalStepRepo.findByRequestTypeAndStepOrder(
                request.getRequestType(), request.getCurrentStep());

        if (nextStep.isEmpty()) {
            request.setStatus(RequestStatus.APPROVED);
        }

        saveHistory(reqId, user.getId(), "APPROVED");
        requestRepo.save(request);
    }

    @Override
    public void reject(Long reqId) {
        User user = getAuthenticatedUser();
        Request request = requestRepo.findById(reqId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Request not found"));

        if (request.getCreatedBy().equals(user.getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Requester cannot reject own request");
        }

        Optional<ApprovalStep> currentStep = approvalStepRepo.findByRequestTypeAndStepOrder(
                request.getRequestType(), request.getCurrentStep());

        if (currentStep.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Workflow step not defined for this request type and step order");
        }

        if (!currentStep.get().getRole().equals(user.getRole())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Role mismatch: This step requires "
                    + currentStep.get().getRole() + " but you are " + user.getRole());
        }

        request.setStatus(RequestStatus.REJECTED);
        saveHistory(reqId, user.getId(), "REJECTED");
        requestRepo.save(request);
    }

    public void adminOverride(Long requestId, String action) {
        User admin = getAuthenticatedUser();

        if (!"ADMIN".equals(admin.getRole().toString())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access Denied: Only admin can override workflows");
        }

        Request request = requestRepo.findById(requestId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Request ID " + requestId + " not found"));

        if ("APPROVE".equalsIgnoreCase(action)) {
            request.setStatus(RequestStatus.APPROVED);
            saveHistory(requestId, admin.getId(), "ADMIN_OVERRIDE_APPROVE");
        } else if ("REJECT".equalsIgnoreCase(action)) {
            request.setStatus(RequestStatus.REJECTED);
            saveHistory(requestId, admin.getId(), "ADMIN_OVERRIDE_REJECT");
        }

        requestRepo.save(request);
    }

    private void saveHistory(Long reqId, Long userId, String action) {
        ApprovalHistory history = new ApprovalHistory();
        history.setRequestId(reqId);
        history.setAction(action);
        history.setActionBy(userId);
        history.setActionAt(LocalDateTime.now());

        approvalHistoryRepo.save(history);
    }

    @Override
    public List<ApprovalHistory> findByRequestId(Long id) {
        List<ApprovalHistory> list = approvalHistoryRepo.findAll();
        return list;
    }
}
