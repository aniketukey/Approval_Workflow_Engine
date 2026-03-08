package com.workflow.service;

import com.workflow.dto.RequestDetailsDto;
import com.workflow.entity.Request;

public interface RequestService {

	Request createRequest(String type);

	RequestDetailsDto getRequestDetails(Long id);

}
