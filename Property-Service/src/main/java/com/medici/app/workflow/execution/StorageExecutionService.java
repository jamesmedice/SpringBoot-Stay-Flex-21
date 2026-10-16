package com.medici.app.workflow.execution;

import com.medici.app.workflow.request.StorageExecutionRequest;
import com.medici.app.workflow.response.StorageExecutionResponse;
import org.springframework.stereotype.Component;

import com.medici.app.workflow.manager.IAction;
import com.medici.app.utils.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class StorageExecutionService implements IAction<StorageExecutionRequest, StorageExecutionResponse> {

	@Override
	public StorageExecutionResponse run(StorageExecutionRequest processParams) {
		log.info("StorageExecutionService.run {}", processParams);

		StorageExecutionResponse response = new StorageExecutionResponse();
		response.setBucket(processParams.getBucket());
		response.setFolder(processParams.getFolder());
		response.setServiceAccount(processParams.getServiceAccount());
		response.setReceived(DateUtils.datenow());
		return response;
	}

	@Override
	public StorageExecutionRequest getMessageRequest() {
		return new StorageExecutionRequest();
	}

	@Override
	public StorageExecutionResponse getMessageResponse() {
		return null;
	}

}
