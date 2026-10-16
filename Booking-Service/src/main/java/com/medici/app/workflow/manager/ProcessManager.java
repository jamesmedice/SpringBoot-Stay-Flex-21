package com.medici.app.workflow.manager;

import com.medici.app.constants.ApplicationConstants;
import com.medici.app.entity.ProcessInstance;
import com.medici.app.exceptions.EventNotPermitted;
import com.medici.app.utils.FileUtils;
import com.medici.app.utils.JsonUtils;
import com.medici.app.workflow.model.Action;
import com.medici.app.workflow.model.Event;
import com.medici.app.workflow.model.Step;
import com.medici.app.workflow.model.Workflowschema;
import com.medici.app.workflow.request.MessageRequest;
import com.medici.app.workflow.response.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Component
public class ProcessManager implements IProcessManager {

    @Autowired
    Map<String, IAction> actions;

    public Step getStep(String processName, String currentStep) {
        Workflowschema schema = getSchema(processName);
        Optional<Step> step = schema.getWorkflow().getSteps().stream().filter(source -> source.getName().equalsIgnoreCase(currentStep)).findFirst();
        return step.orElse(null);
    }

    public Event processEvent(ProcessInstance processInstance, String eventName, Map<String, MessageRequest> request) throws EventNotPermitted {
        Workflowschema schema = getSchema(processInstance.getProcessName());

        Optional<Step> step = schema.getWorkflow().getSteps().stream().filter(source -> source.getName().equalsIgnoreCase(processInstance.getCurrentStep())).findFirst();
        Step stepModel = step.get();
        log.info("Step-model : {}", stepModel.toString());
        log.info("EventName : {} ", eventName);

        Optional<Event> event = stepModel.getEvents().stream().filter(source -> source.getName().equalsIgnoreCase(eventName)).findFirst();

        if (event.isEmpty())
            throw new EventNotPermitted(MessageFormat.format(ApplicationConstants.EVENT_NOT_PERMITED, eventName));

        log.info("Event : {} ", event.get().toString());

        if (event.get().getActions() != null && !CollectionUtils.isEmpty(event.get().getActions()))
            for (Action action : event.get().getActions()) {
                log.info(MessageFormat.format(ApplicationConstants.RUNNING_METHOD_BY_FLOW, action.getActionClass(), action.getActionName(), request));

                IAction triggerAction = actions.get(action.getActionClass());
                MessageRequest messageRequest = request.get(action.getActionName());
                MessageResponse messageResponse = (MessageResponse) triggerAction.run(messageRequest);
                event.get().getActionsResponse().put(messageRequest.getType(), messageResponse);
            }

        return event.orElse(null);
    }

    public IAction actionClass(String actionName) {
        IAction triggerAction = actions.get(actionName);
        return triggerAction;
    }

    public Workflowschema getSchema(String processName) {
        InputStream schemaStream = FileUtils.getFileAsIOStream(processName);
        Workflowschema Schema = JsonUtils.jsonStringToObject(schemaStream, Workflowschema.class);
        return Schema;
    }

}
