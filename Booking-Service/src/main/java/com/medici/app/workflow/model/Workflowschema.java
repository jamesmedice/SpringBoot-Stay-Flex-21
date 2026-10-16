
package com.medici.app.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Workflowschema implements Serializable {

    private String version;
    private String processName;
    private String startActivity;
    private Workflow workflow;

}
