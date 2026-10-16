package com.medici.app.workflow.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompleteExecutionResponse implements MessageResponse {

    private String outcome;
    private String result;
    private String datasource;

    private Date received;
    private Date endProcess;

}
