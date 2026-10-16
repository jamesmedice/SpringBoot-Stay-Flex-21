
package com.medici.app.workflow.model;

import com.medici.app.workflow.response.MessageResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event implements Serializable {

    private String name;
    private String destination;
    private List<Action> actions = new ArrayList<Action>();
    private Map<String, MessageResponse> actionsResponse = new HashMap<>();
}
