package com.medici.app.workflow.manager;

public interface IAction<MessageRequest, MessageResponse> {

	MessageResponse run(MessageRequest processParams);

	MessageRequest getMessageRequest();

	MessageResponse getMessageResponse();
}
