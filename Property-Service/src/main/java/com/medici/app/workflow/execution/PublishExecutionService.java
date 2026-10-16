package com.medici.app.workflow.execution;

import com.medici.app.utils.DateUtils;
import com.medici.app.workflow.manager.IAction;
import com.medici.app.workflow.request.PublishExecutionRequest;
import com.medici.app.workflow.response.PublishExecutionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PublishExecutionService implements IAction<PublishExecutionRequest, PublishExecutionResponse> {

    @Override
    public PublishExecutionResponse run(PublishExecutionRequest processParams) {
        log.info("Publish.run {}", processParams);

        PublishExecutionResponse response = new PublishExecutionResponse();
        response.setSubject(processParams.getSubject());
        response.setBody(processParams.getBody());
        response.setTitle(processParams.getTitle());
        response.setEmail(processParams.getEmail());
        response.setReceived(DateUtils.datenow());
        return response;
    }

    @Override
    public PublishExecutionRequest getMessageRequest() {
        return new PublishExecutionRequest();
    }

    @Override
    public PublishExecutionResponse getMessageResponse() {
        return new PublishExecutionResponse();
    }

}
