
package com.medici.app.workflow.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Step implements Serializable {

    private String name;
    private List<Event> events = new ArrayList<Event>();

}
