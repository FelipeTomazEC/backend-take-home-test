package com.noom.interview.fullstack.sleep.application.ports.repositories;

import com.noom.interview.fullstack.sleep.domain.SleepLog;

import java.util.UUID;

public interface SaveSleepLogRepository {
    void save(SleepLog sleepLog, UUID userId);
}
