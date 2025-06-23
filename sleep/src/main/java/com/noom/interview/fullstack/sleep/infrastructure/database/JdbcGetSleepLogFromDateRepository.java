package com.noom.interview.fullstack.sleep.infrastructure.database;

import com.noom.interview.fullstack.sleep.application.ports.repositories.GetSleepLogFromDateRepository;
import com.noom.interview.fullstack.sleep.domain.SleepLog;
import com.noom.interview.fullstack.sleep.infrastructure.database.mappers.SleepLogRowMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Repository
@RequiredArgsConstructor
public class JdbcGetSleepLogFromDateRepository implements GetSleepLogFromDateRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Override
    public Optional<SleepLog> findByDate(LocalDate date, UUID userId) {
        var sql = "SELECT * FROM sleep_logs WHERE user_id = :userId AND sleep_date = :date";
        var params = Map.of(
                "userId", userId,
                "date", date
        );

        try {
            var sleepLog = jdbcTemplate.queryForObject(sql, params, new SleepLogRowMapper());

            return Optional.ofNullable(sleepLog);
        } catch (EmptyResultDataAccessException e) {
            log.info("No logs found:  userId={}, date={}", userId, date);
            return Optional.empty();
        }
    }
}
