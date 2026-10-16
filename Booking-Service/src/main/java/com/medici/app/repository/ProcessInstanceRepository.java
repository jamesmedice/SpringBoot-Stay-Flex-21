package com.medici.app.repository;

import com.medici.app.entity.ProcessInstance;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProcessInstanceRepository extends CrudRepository<ProcessInstance, UUID> {

}
