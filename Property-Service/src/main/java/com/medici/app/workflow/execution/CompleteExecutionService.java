package com.medici.app.workflow.execution;

import com.medici.app.utils.DateUtils;
import com.medici.app.workflow.manager.IAction;
import com.medici.app.workflow.request.CompleteExecutionRequest;
import com.medici.app.workflow.response.CompleteExecutionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CompleteExecutionService implements IAction<CompleteExecutionRequest, CompleteExecutionResponse> {

    @Override
    public CompleteExecutionResponse run(CompleteExecutionRequest processParams) {
        log.info("Complete.run {}", processParams);

        CompleteExecutionResponse response = new CompleteExecutionResponse();
        response.setReceived(DateUtils.datenow());
        response.setDatasource(processParams.getDatasource());
        response.setOutcome(processParams.getOutcome());
        response.setResult(processParams.getResult());
        response.setEndProcess(DateUtils.datenow());
        return response;
    }

    @Override
    public CompleteExecutionRequest getMessageRequest() {
        return new CompleteExecutionRequest();
    }

    @Override
    public CompleteExecutionResponse getMessageResponse() {
        return null;
    }


}
