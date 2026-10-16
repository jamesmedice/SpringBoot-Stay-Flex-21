package com.medici.app.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Event_History", schema = "WORKFLOW")
@IdClass(EventHistoryId.class)
public class EventHistory {

	@Id
	@Column(name = "process_id", nullable = false)
	private UUID processId;

	@Column(name = "event_name", nullable = false)
	private String eventName;

	@Column(name = "event_time", nullable = false)
	private Date eventTime;

	@Column(name = "destination_step")
	private String destinationStep;

	@Column(name = "event_user")
	private String eventUser;

	@Column(name = "event_note")
	private String eventNote;

}
