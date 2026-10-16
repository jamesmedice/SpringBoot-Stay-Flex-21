package com.medici.app.workflow.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StorageExecutionResponse implements MessageResponse {

    private String bucket;
    private String folder;
    private String serviceAccount;

    private Date received;

}
