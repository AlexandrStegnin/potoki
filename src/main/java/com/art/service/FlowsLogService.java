package com.art.service;

import com.art.model.supporting.FlowsLog;
import com.art.repository.FlowsLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Service
public class FlowsLogService {

    private final FlowsLogRepository flowsLogRepository;

    public FlowsLogService(FlowsLogRepository flowsLogRepository) {
        this.flowsLogRepository = flowsLogRepository;
    }

    public FlowsLog create(FlowsLog flowsLog) {
        return flowsLogRepository.save(flowsLog);
    }

    public FlowsLog findOne(Long flowsLogId) {
        return flowsLogRepository.findOne(flowsLogId);
    }

    public FlowsLog update(FlowsLog flowsLog) {
        return flowsLogRepository.saveAndFlush(flowsLog);
    }

    public void delete(FlowsLog flowsLog) {
        flowsLogRepository.delete(flowsLog);
    }

    public List<FlowsLog> saveAll(List<FlowsLog> flowsLogs) {
        return flowsLogRepository.save(flowsLogs);
    }

}
