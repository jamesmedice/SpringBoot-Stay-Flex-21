package com.medici.app.workflow.manager;

import com.medici.app.entity.ProcessInstance;
import com.medici.app.exceptions.EventNotPermitted;
import com.medici.app.workflow.model.Event;
import com.medici.app.workflow.model.Step;
import com.medici.app.workflow.model.Workflowschema;
import com.medici.app.workflow.request.MessageRequest;

import java.util.Map;

public interface IProcessManager {

    public Workflowschema getSchema(String processName);

    public Event processEvent(ProcessInstance processInstance, String eventName, Map<String, MessageRequest> request) throws EventNotPermitted;

    public Step getStep(String processName, String currentStep);

    public IAction actionClass(String actionName);
}
