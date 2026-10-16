package com.medici.app.workflow.execution;

import com.medici.app.workflow.request.SubscribeExecutionRequest;
import com.medici.app.workflow.response.SubscribeExecutionResponse;
import org.springframework.stereotype.Component;

import com.medici.app.workflow.manager.IAction;
import com.medici.app.utils.DateUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SubscribeExecutionService implements IAction<SubscribeExecutionRequest, SubscribeExecutionResponse> {

	@Override
	public SubscribeExecutionResponse run(SubscribeExecutionRequest processParams) {
		log.info("Subscribe.run {}", processParams);

		SubscribeExecutionResponse response = new SubscribeExecutionResponse();
		response.setTopic(processParams.getTopic());
		response.setSubscription(processParams.getSubscription());
		response.setReceived(DateUtils.datenow());
		return response;
	}

	@Override
	public SubscribeExecutionRequest getMessageRequest() {
		return new SubscribeExecutionRequest();
	}

	@Override
	public SubscribeExecutionResponse getMessageResponse() {
		return new SubscribeExecutionResponse();
	}

}
