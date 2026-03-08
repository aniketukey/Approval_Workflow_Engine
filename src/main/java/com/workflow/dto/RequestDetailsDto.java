package com.workflow.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDetailsDto {
	
	    private Long requestId;

	    private String requestType;

	    private String status;

	    private Long createdBy;

	    private LocalDateTime createdAt;

	    private Integer currentStep;

	    private String currentApproverRole;
	

}
