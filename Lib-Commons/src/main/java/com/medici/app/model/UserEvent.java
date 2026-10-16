package com.medici.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserEvent {

    private String uuid;

    private String eventName;

    private Date eventTime;

    private String detail;
}
