package com.noom.interview.fullstack.sleep.infrastructure.database;

import com.noom.interview.fullstack.sleep.application.ports.repositories.GetSleepLogFromDateRepository;
import com.noom.interview.fullstack.sleep.domain.SleepLog;
import com.noom.interview.fullstack.sleep.domain.SleepQuality;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
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
            var sleepLog = jdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> {
                var bedTime = rs.getObject("bed_time", LocalTime.class);
                var wakeUpTime = rs.getObject("wake_up_time", LocalTime.class);
                var sleepDate = rs.getObject("sleep_date", LocalDate.class);
                var quality = SleepQuality.valueOf(rs.getString("quality"));

                return SleepLog.builder()
                        .bedTime(sleepDate.minusDays(1).atTime(bedTime))
                        .wakeUpTime(sleepDate.atTime(wakeUpTime))
                        .quality(quality)
                        .build();
                    }
            );

            return Optional.ofNullable(sleepLog);
        } catch (EmptyResultDataAccessException e) {
            log.info("No logs found:  userId={}, date={}", userId, date);
            return Optional.empty();
        }
    }
}
