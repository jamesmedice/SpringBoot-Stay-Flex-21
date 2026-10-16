package com.medici.app.resource;

import com.medici.app.entity.ProcessInstance;
import com.medici.app.exceptions.ActionError;
import com.medici.app.exceptions.EventNotPermitted;
import com.medici.app.exceptions.ProcessNotFound;
import com.gft.elw.model.*;
import com.medici.app.model.*;
import com.medici.app.service.WorkflowService;
import com.medici.app.workflow.request.MessageRequest;
import com.medici.app.workflow.model.Action;
import com.medici.app.workflow.model.Event;
import com.medici.app.workflow.model.Step;
import com.medici.app.workflow.model.Workflow;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@RestController
public class WorkflowController {

    @Autowired
    WorkflowService workflowService;

    @Operation(
            operationId = "startWorkflowUsingGET",
            summary = "startWorkflow",
            description = "startWorkflow",
            tags = {"Workflow"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProcessInstanceResponse.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @RequestMapping(method = RequestMethod.GET, value = "/start/{processName}", produces = {"application/json"})
    public ResponseEntity<ProcessInstanceResponse> startWorkflowUsingGET(@Parameter(name = "processName", description = "processName", required = true, in = ParameterIn.PATH) @PathVariable("processName") String processName) {
        try {
            ProcessInstance response = workflowService.start(processName);
            ProcessInstanceResponse payload = new ProcessInstanceResponse();
            BeanUtils.copyProperties(response, payload);
            return new ResponseEntity(payload, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(
            operationId = "singleProcessEventWorkflowUsingPOST",
            summary = "singleProcessEventWorkflow",
            description = "singleProcessEventWorkflow",
            tags = {"Workflow"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = EventHistoryResponse.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @RequestMapping(method = RequestMethod.POST, value = "/singleprocess", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<Event> singleProcessEventWorkflowUsingPOST(@Parameter(name = "start", description = "start", required = true) @Valid @RequestBody SingleProcessEventRequest singleProcessEventRequest) {
        try {
            Map<String, MessageRequest> request = new HashMap();
            request.put(singleProcessEventRequest.getActionName(), singleProcessEventRequest.getMessageRequest());

            Event payload = workflowService.processEvent(singleProcessEventRequest.getProcessUuid(), singleProcessEventRequest.getEventName(), request);
            return new ResponseEntity(payload, HttpStatus.OK);
        } catch (ProcessNotFound | EventNotPermitted | ActionError e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(
            operationId = "multipleprocessEventWorkflowUsingPOST",
            summary = "multipleprocessEventWorkflow",
            description = "multipleprocessEventWorkflow",
            tags = {"Workflow"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = EventHistoryResponse.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @RequestMapping(method = RequestMethod.POST, value = "/multipleprocess", produces = {"application/json"}, consumes = {"application/json"})
    public ResponseEntity<Event> multipleprocessEventWorkflowUsingPOST(@Parameter(name = "start", description = "start", required = true) @Valid @RequestBody MultipleProcessEventRequest start) {
        try {
            Map<String, MessageRequest> request = new HashMap();

            for (AdditionalProperties source : start.getParams())
                request.put(source.getActionName(), source.getMessageRequest());

            Event payload = workflowService.processEvent(start.getProcessUuid(), start.getEventName(), request);
            return new ResponseEntity(payload, HttpStatus.OK);
        } catch (ProcessNotFound | EventNotPermitted | ActionError e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(
            operationId = "loadWorkflowUsingGET",
            summary = "loadWorkflow",
            description = "loadWorkflow",
            tags = {"Workflow"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProcessInstanceResponse.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @RequestMapping(method = RequestMethod.GET, value = "/process/{uuid}", produces = {"application/json"})
    public ResponseEntity<ProcessInstanceResponse> findProcessInstanceUsingGET(@Parameter(name = "uuid", description = "UUID", required = true, in = ParameterIn.PATH) @PathVariable("uuid") UUID uuid) {
        try {
            ProcessInstance response = workflowService.findProcessInstance(uuid);
            ProcessInstanceResponse payload = new ProcessInstanceResponse();
            BeanUtils.copyProperties(response, payload);
            return new ResponseEntity(payload, HttpStatus.OK);
        } catch (ProcessNotFound e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(
            operationId = "loadWorkflowUsingGET",
            summary = "loadWorkflow",
            description = "loadWorkflow",
            tags = {"Workflow"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProcessInstanceResponse.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @RequestMapping(method = RequestMethod.GET, value = "/workflow/{uuid}", produces = {"application/json"})
    public ResponseEntity<?> loadWorkflowUsingGET(@Parameter(name = "uuid", description = "UUID", required = true, in = ParameterIn.PATH) @PathVariable("uuid") UUID uuid) {
        try {
            Workflow payload = workflowService.workflow(uuid);
            return new ResponseEntity(payload, HttpStatus.OK);
        } catch (ProcessNotFound e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(
            operationId = "findStepUsingGET",
            summary = "findStep",
            description = "loadWorkflow",
            tags = {"Workflow"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Step.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @RequestMapping(method = RequestMethod.GET, value = "/step/{uuid}", produces = {"application/json"})
    public ResponseEntity<?> findStepUsingGET(@Parameter(name = "uuid", description = "UUID", required = true, in = ParameterIn.PATH) @PathVariable("uuid") UUID uuid) {
        try {
            Step step = workflowService.step(uuid);
            return new ResponseEntity(step, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(
            operationId = "findEventUsingGET",
            summary = "findEvent",
            description = "loadWorkflow",
            tags = {"Workflow"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Step.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @RequestMapping(method = RequestMethod.GET, value = "/event/{uuid}/{eventName}", produces = {"application/json"})
    public ResponseEntity<?> findEventUsingGET(@Parameter(name = "uuid", description = "UUID", required = true, in = ParameterIn.PATH) @PathVariable("uuid") UUID uuid,
                                               @Parameter(name = "eventName", required = true, in = ParameterIn.PATH) @PathVariable("eventName") String eventName) {
        try {
            Event payload = workflowService.event(uuid, eventName);
            return new ResponseEntity(payload, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(
            operationId = "findActionUsingGET",
            summary = "findAction",
            description = "loadWorkflow",
            tags = {"Workflow"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = Step.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @RequestMapping(method = RequestMethod.GET, value = "/action/{uuid}/{eventName}/{actionClass}", produces = {"application/json"})
    public ResponseEntity<?> findActionUsingGET(@Parameter(name = "uuid", description = "UUID", required = true, in = ParameterIn.PATH) @PathVariable("uuid") UUID uuid,
                                                @Parameter(name = "eventName", required = true, in = ParameterIn.PATH) @PathVariable("eventName") String eventName,
                                                @Parameter(name = "actionClass", required = true, in = ParameterIn.PATH) @PathVariable("actionClass") String actionClass) {
        try {
            Action action = workflowService.action(uuid, eventName, actionClass);
            ActionResponse payload = new ActionResponse();
            BeanUtils.copyProperties(action, payload);
            payload.setMessageRequest(workflowService.actionRequest(actionClass));
            return new ResponseEntity(payload, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(
            operationId = "cancelWorkflowUsingGET",
            summary = "cancelWorkflow",
            description = "cancelWorkflow",
            tags = {"Workflow"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProcessInstanceResponse.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @RequestMapping(method = RequestMethod.GET, value = "/cancel/{uuid}", produces = {"application/json"})
    public ResponseEntity<ProcessInstanceResponse> cancelWorkflowUsingGET(@Parameter(name = "uuid", description = "UUID", required = true, in = ParameterIn.PATH) @PathVariable("uuid") UUID uuid) {
        try {
            ProcessInstance response = workflowService.cancel(UUID.fromString(uuid.toString()));
            ProcessInstanceResponse payload = new ProcessInstanceResponse();
            BeanUtils.copyProperties(response, payload);
            return new ResponseEntity(payload, HttpStatus.OK);
        } catch (ProcessNotFound | EventNotPermitted | ActionError e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @Operation(
            operationId = "endWorkflowUsingGET",
            summary = "endWorkflow",
            description = "endWorkflow",
            tags = {"Workflow"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "OK", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = ProcessInstanceResponse.class))
                    }),
                    @ApiResponse(responseCode = "401", description = "Unauthorized"),
                    @ApiResponse(responseCode = "403", description = "Forbidden"),
                    @ApiResponse(responseCode = "404", description = "Not Found")
            }
    )
    @RequestMapping(method = RequestMethod.GET, value = "/end/{uuid}", produces = {"application/json"})
    public ResponseEntity<ProcessInstanceResponse> endWorkflowUsingGET(@Parameter(name = "uuid", description = "UUID", required = true, in = ParameterIn.PATH) @PathVariable("uuid") UUID uuid) {

        try {
            ProcessInstance response = workflowService.end(uuid);
            ProcessInstanceResponse payload = new ProcessInstanceResponse();
            BeanUtils.copyProperties(response, payload);
            return new ResponseEntity(payload, HttpStatus.OK);
        } catch (ProcessNotFound | EventNotPermitted | ActionError e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }


}
