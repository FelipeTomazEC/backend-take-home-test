package com.noom.interview.fullstack.sleep.infrastructure.database;

import com.noom.interview.fullstack.sleep.application.ports.repositories.SaveSleepLogRepository;
import com.noom.interview.fullstack.sleep.domain.SleepLog;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JdbcSleepLogSaveRepository implements SaveSleepLogRepository {

    private final DataSource dataSource;

    @Override
    public void save(SleepLog sleepLog, UUID userId) {
        var insertQuery = new SimpleJdbcInsert(dataSource)
                .withTableName("sleep_logs")
                .usingGeneratedKeyColumns("id");

        var params = Map.of(
                "user_id", userId,
                "sleep_date", sleepLog.getSleepDate(),
                "bed_time", sleepLog.getBedTime(),
                "wake_up_time", sleepLog.getWakeUpTime(),
                "quality", sleepLog.getQuality().name()
        );

        insertQuery.execute(params);
    }
}
