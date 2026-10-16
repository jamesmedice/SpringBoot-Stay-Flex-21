package com.medici.app.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventHistoryId implements Serializable {

	private UUID processId;
	private String eventName;
	private Date eventTime;
}
