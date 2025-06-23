package com.noom.interview.fullstack.sleep.infrastructure.database;

import com.noom.interview.fullstack.sleep.application.ports.repositories.GetLogsFromPeriodRepository;
import com.noom.interview.fullstack.sleep.domain.SleepLog;
import com.noom.interview.fullstack.sleep.infrastructure.database.mappers.SleepLogRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcGetLogsFromPeriodRepository implements GetLogsFromPeriodRepository {

    public final NamedParameterJdbcTemplate jdbcTemplate;

    public List<SleepLog> findByPeriod(LocalDate startDate, LocalDate endDate, UUID userId) {
        var sql = "SELECT * FROM sleep_logs WHERE user_id = :userId AND sleep_date >= :startDate AND sleep_date <= :endDate";
        var params = Map.of(
                "userId", userId,
                "startDate", startDate,
                "endDate", endDate
        );

        return jdbcTemplate.query(sql, params, new SleepLogRowMapper());
    }
}
