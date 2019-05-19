package com.art.repository;

import com.art.model.supporting.FlowsLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FlowsLogRepository extends JpaRepository<FlowsLog, Long> {
}
