package com.workflow.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.workflow.dto.RequestDetailsDto;
import com.workflow.entity.Request;
import com.workflow.service.RequestService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/requests")
@RequiredArgsConstructor
public class RequestController {

	private final RequestService requestService;

	@PreAuthorize("hasRole('REQUESTER')")
	@PostMapping("/create")
	public Request createRequest(@RequestParam String type) {

		return requestService.createRequest(type);
	}

	@PreAuthorize("hasAnyRole('ADMIN' , 'REQUESTER' , 'APPROVER')")
	@GetMapping("/details/{id}")
	public ResponseEntity<?> getRequest(@PathVariable Long id) {

		RequestDetailsDto response = requestService.getRequestDetails(id);

		return ResponseEntity.ok(response);
	}

}
