package com.medici.app.service;

import com.medici.app.constants.ApplicationConstants;
import com.medici.app.constants.ProcessStatus;
import com.medici.app.entity.EventHistory;
import com.medici.app.entity.ProcessInstance;
import com.medici.app.exceptions.ActionError;
import com.medici.app.exceptions.EventNotPermitted;
import com.medici.app.exceptions.ProcessNotFound;
import com.medici.app.utils.DateUtils;
import com.medici.app.workflow.manager.IAction;
import com.medici.app.workflow.manager.IProcessManager;
import com.gft.elw.workflow.model.*;
import com.medici.app.workflow.model.*;
import com.medici.app.workflow.request.MessageRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class WorkflowService {

    @Autowired
    IProcessManager iProcessManager;

    @Autowired
    ProcessService processService;

    public ProcessInstance start(String processName) {
        Workflowschema schema = iProcessManager.getSchema(processName);

        UUID processUuid = UUID.randomUUID();

        EventHistory eventHistory = new EventHistory();
        eventHistory.setDestinationStep(schema.getStartActivity());
        eventHistory.setEventName(schema.getProcessName());
        eventHistory.setEventTime(DateUtils.datenow());
        eventHistory.setEventUser(null);
        eventHistory.setProcessId(processUuid);
        eventHistory = processService.save(eventHistory);
        log.info(eventHistory.toString());

        ProcessInstance processInstance = new ProcessInstance();
        processInstance.setClosingTime(DateUtils.datenow());
        processInstance.setCreationTime(DateUtils.datenow());
        processInstance.setCurrentStep(schema.getStartActivity());
        processInstance.setProcessStatus(ProcessStatus.OPEN.toString());
        processInstance.setCurrentStepEntryTime(DateUtils.datenow());
        processInstance.setProcessId(processUuid);
        processInstance.setProcessName(processName);
        processInstance.setProcessVersion(schema.getVersion());
        processInstance.setUpdateTime(DateUtils.datenow());
        processInstance = processService.save(processInstance);
        log.info(processInstance.toString());

        return processInstance;
    }

    public Event processEvent(UUID processUuid, String eventName, Map<String, MessageRequest> messageRequest) throws ProcessNotFound, EventNotPermitted, ActionError {

        ProcessInstance processInstance = findProcessInstance(processUuid);

        log.info(processInstance.toString());

        validateProcessStatus(processInstance);

        Event event = iProcessManager.processEvent(processInstance, eventName, messageRequest);

        processInstance.setCurrentStep(event.getDestination());
        processInstance.setUpdateTime(DateUtils.datenow());
        processService.save(processInstance);

        EventHistory eventHistoryEntity = new EventHistory();
        eventHistoryEntity.setProcessId(processUuid);
        eventHistoryEntity.setEventName(eventName);
        eventHistoryEntity.setEventTime(DateUtils.datenow());
        eventHistoryEntity.setDestinationStep(event.getDestination());
        processService.save(eventHistoryEntity);

        return event;
    }

    public ProcessInstance findProcessInstance(UUID processUuid) throws ProcessNotFound {
        Optional<ProcessInstance> processInstance = processService.findProcessInstanceById(processUuid);
        if (processInstance.isEmpty())
            throw new ProcessNotFound(MessageFormat.format(ApplicationConstants.ENTITY_NOT_FOUND, processUuid.toString()));

        return processInstance.get();
    }

    public Workflow workflow(UUID processUuid) throws ProcessNotFound {
        ProcessInstance processInstance = findProcessInstance(processUuid);

        Workflowschema schema = iProcessManager.getSchema(processInstance.getProcessName());
        return schema.getWorkflow();
    }

    public Step step(UUID processUuid) throws ProcessNotFound {
        ProcessInstance processInstance = findProcessInstance(processUuid);
        return step(processInstance.getProcessName(), processInstance.getCurrentStep());
    }

    public Step step(String processName, String currentStep) {
        return iProcessManager.getStep(processName, currentStep);
    }

    public Event event(UUID processUuid, String eventName) throws ProcessNotFound {
        ProcessInstance processInstance = findProcessInstance(processUuid);
        return event(processInstance.getProcessName(), processInstance.getCurrentStep(), eventName);
    }

    public Event event(String processName, String currentStep, String eventName) {
        Step step = step(processName, currentStep);
        return step.getEvents().stream().filter(event -> event.getName().equalsIgnoreCase(eventName)).findFirst().orElse(null);
    }

    public Action action(UUID processUuid, String eventName, String actionName) throws ProcessNotFound {
        ProcessInstance processInstance = findProcessInstance(processUuid);
        return action(processInstance.getProcessName(), processInstance.getCurrentStep(), eventName, actionName);
    }

    public Action action(String processName, String currentStep, String eventName, String actionName) {
        Event event = event(processName, currentStep, eventName);
        return event.getActions().stream().filter(action ->
                action.getActionName().equalsIgnoreCase(actionName)).findFirst().orElse(null);
    }

    public MessageRequest actionRequest(String actionClass) {
        IAction iAcction = iProcessManager.actionClass(actionClass);
        return (MessageRequest) iAcction.getMessageRequest();
    }

    public ProcessInstance cancel(UUID processUuid) throws ProcessNotFound, EventNotPermitted, ActionError {

        ProcessInstance processInstance = findProcessInstance(processUuid);

        validateProcessStatus(processInstance);

        processInstance.setClosingTime(DateUtils.datenow());
        processInstance.setUpdateTime(DateUtils.datenow());
        processInstance.setProcessStatus(ProcessStatus.CANCELED.toString());
        processService.save(processInstance);

        EventHistory eventHistoryEntity = new EventHistory();
        eventHistoryEntity.setProcessId(processUuid);
        eventHistoryEntity.setEventName(ProcessStatus.CANCELED.toString());
        eventHistoryEntity.setEventTime(DateUtils.datenow());
        eventHistoryEntity.setDestinationStep(null);
        processService.save(eventHistoryEntity);

        return processInstance;
    }

    public ProcessInstance end(UUID processUuid) throws ProcessNotFound, EventNotPermitted, ActionError {
        ProcessInstance processInstance = findProcessInstance(processUuid);

        validateProcessStatus(processInstance);

        processInstance.setClosingTime(DateUtils.datenow());
        processInstance.setUpdateTime(DateUtils.datenow());
        processInstance.setProcessStatus(ProcessStatus.CLOSED.toString());
        processService.save(processInstance);

        EventHistory eventHistoryEntity = new EventHistory();
        eventHistoryEntity.setProcessId(processUuid);
        eventHistoryEntity.setEventName(ProcessStatus.CLOSED.toString());
        eventHistoryEntity.setEventTime(DateUtils.datenow());
        processService.save(eventHistoryEntity);

        return processInstance;
    }

    private static void validateProcessStatus(ProcessInstance processInstance) throws EventNotPermitted {
        if (!processInstance.getProcessStatus().equalsIgnoreCase(ProcessStatus.OPEN.toString()))
            throw new EventNotPermitted(MessageFormat.format(ApplicationConstants.EVENT_NOT_PERMITED, processInstance.getProcessStatus()));
    }

}
