package com.medici.app.workflow.request;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = CompleteExecutionRequest.class, name = "CompleteExecution"),
        @JsonSubTypes.Type(value = PublishExecutionRequest.class, name = "PublishExecution"),
        @JsonSubTypes.Type(value = StorageExecutionRequest.class, name = "StorageExecution"),
        @JsonSubTypes.Type(value = SubscribeExecutionRequest.class, name = "SubscribeExecution")
})
public interface MessageRequest extends Serializable {
    String getType();
}
