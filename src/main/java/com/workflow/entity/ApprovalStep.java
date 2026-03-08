package com.workflow.entity;

import com.workflow.entity.enums.RequestType;
import com.workflow.entity.enums.Role;

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

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApprovalStep {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column
	@Enumerated(EnumType.STRING)
    private RequestType requestType;

	@Column
    private Integer stepOrder;

    @Column
	@Enumerated(EnumType.STRING)
    private Role role;

}
