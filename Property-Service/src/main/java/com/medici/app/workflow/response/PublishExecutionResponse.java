package com.medici.app.workflow.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublishExecutionResponse implements MessageResponse {

    private String title;
    private String subject;
    private String body;
    private String email;

    private Date received;

}
