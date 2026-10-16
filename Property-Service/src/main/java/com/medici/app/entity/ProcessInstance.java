package com.medici.app.entity;

import java.util.Date;
import java.util.UUID;


import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Process_Instance" , schema = "WORKFLOW")
public class ProcessInstance {

	@Id
	@Column(name = "process_id", nullable = false)
	private UUID processId;

	@Column(name = "process_name", nullable = false)
	private String processName;

	@Column(name = "process_version", nullable = false)
	private String processVersion;

	@Column(name = "process_status", nullable = false)
	private String processStatus;

	@Column(name = "current_step", nullable = false)
	private String currentStep;

	@Column(name = "parent_process_id")
	private String parentProcessId;

	@Column(name = "creation_time", nullable = false)
	private Date creationTime;

	@Column(name = "current_step_entry_time", nullable = false)
	private Date currentStepEntryTime;

	@Column(name = "closing_time", nullable = false)
	private Date closingTime;

	@Column(name = "update_time", nullable = false)
	private Date updateTime;

}
