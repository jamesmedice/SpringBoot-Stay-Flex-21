package com.medici.app.service;

import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.medici.app.entity.EventHistory;
import com.medici.app.entity.ProcessInstance;
import com.medici.app.repository.EventHistoryRepository;
import com.medici.app.repository.ProcessInstanceRepository;

@Service
public class ProcessService {

	@Autowired
	ProcessInstanceRepository processInstanceRepository;

	@Autowired
	EventHistoryRepository eventHistoryRepository;

	public ProcessInstance save(ProcessInstance entity) {
		return processInstanceRepository.save(entity);
	}

	public EventHistory save(EventHistory entity) {
		return eventHistoryRepository.save(entity);
	}

	public Optional<ProcessInstance> findProcessInstanceById(UUID processUuid) {
		Optional<ProcessInstance> entity = processInstanceRepository.findById(processUuid);
		return entity;
	}
}