package com.workflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workflow.entity.ApprovalHistory;

public interface ApprovalHistoryRepo extends JpaRepository<ApprovalHistory, Long> {

}
