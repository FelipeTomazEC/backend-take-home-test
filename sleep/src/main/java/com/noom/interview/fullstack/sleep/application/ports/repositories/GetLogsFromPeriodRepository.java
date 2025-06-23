package com.noom.interview.fullstack.sleep.application.ports.repositories;

import com.noom.interview.fullstack.sleep.domain.SleepLog;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface GetLogsFromPeriodRepository {
    List<SleepLog> findByPeriod(LocalDate startDate, LocalDate endDate, UUID userId);
}
