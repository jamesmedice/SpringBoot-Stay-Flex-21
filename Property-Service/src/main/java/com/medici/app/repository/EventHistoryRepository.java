package com.medici.app.repository;

import com.medici.app.entity.EventHistory;
import com.medici.app.entity.EventHistoryId;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventHistoryRepository extends CrudRepository<EventHistory, EventHistoryId> {

}
