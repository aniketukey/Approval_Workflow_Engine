package com.workflow.service;

import java.util.List;

import com.workflow.entity.ApprovalHistory;

public interface ApprovalService {

	void approve(Long reqId);

	void reject(Long reqId);

	void adminOverride(Long id, String action);

	List<ApprovalHistory> findByRequestId(Long id);

}
