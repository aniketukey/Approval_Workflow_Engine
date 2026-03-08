package com.workflow.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ApprovalHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column( nullable = false)
	private Long requestId;

	@Column( nullable = false)
    private String action;
	
	@Column(nullable = false)
    private Long actionBy;

	@CreationTimestamp
	@Column(nullable = false)
    private LocalDateTime actionAt;
}
