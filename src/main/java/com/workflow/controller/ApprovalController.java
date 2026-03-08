package com.workflow.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.workflow.entity.ApprovalHistory;
import com.workflow.service.ApprovalService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/requests")
public class ApprovalController {

	private final ApprovalService approvalService;

	@PreAuthorize("hasRole('APPROVER')")
	@PostMapping("/{id}/approve")
	public ResponseEntity<?> approve(@PathVariable Long id) {
		approvalService.approve(id);
		return ResponseEntity.ok("Approved");
	}

	@PreAuthorize("hasRole('APPROVER')")
	@PostMapping("/{id}/reject")
	public ResponseEntity<?> reject(@PathVariable Long id) {
		approvalService.reject(id);
		return ResponseEntity.ok("Rejected");
	}

	@PreAuthorize("hasRole('ADMIN')")
	@PostMapping("/{id}/admin")
	public void override(@PathVariable Long id, @RequestParam String action) {

		approvalService.adminOverride(id, action);
	}

	@PreAuthorize("hasAnyRole('REQUESTER','ADMIN')")
	@GetMapping("/history/{id}")
	public List<ApprovalHistory> history(@PathVariable Long id) {

		return approvalService.findByRequestId(id);
	}

}
