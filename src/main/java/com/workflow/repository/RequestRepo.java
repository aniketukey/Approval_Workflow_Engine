package com.workflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.workflow.entity.Request;

public interface RequestRepo extends JpaRepository<Request, Long>{

}
