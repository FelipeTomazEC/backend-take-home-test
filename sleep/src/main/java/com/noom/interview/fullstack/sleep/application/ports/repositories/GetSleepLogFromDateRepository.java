package com.noom.interview.fullstack.sleep.application.ports.repositories;

import com.noom.interview.fullstack.sleep.domain.SleepLog;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface GetSleepLogFromDateRepository {
    Optional<SleepLog> findByDate(LocalDate date, UUID userId);
}
