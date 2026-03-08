package com.workflow.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.workflow.entity.enums.RequestStatus;
import com.workflow.entity.enums.RequestType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Request {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "REQUEST_TYPE")
	@Enumerated(EnumType.STRING)
	private RequestType requestType;

	@Column(name = "STATUS")
	@Enumerated(EnumType.STRING)
	private RequestStatus status;

	@Column(name = "CREATED_BY", nullable = false)
	private Long createdBy;

	@CreationTimestamp
	@Column(name = "CREATED_AT", nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "CURRENT_STEP", nullable = false)
	private Integer currentStep;

}
