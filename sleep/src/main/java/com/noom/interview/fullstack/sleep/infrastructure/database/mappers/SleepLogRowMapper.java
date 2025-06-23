package com.noom.interview.fullstack.sleep.infrastructure.database.mappers;

import com.noom.interview.fullstack.sleep.domain.SleepLog;
import com.noom.interview.fullstack.sleep.domain.SleepQuality;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class SleepLogRowMapper implements RowMapper<SleepLog> {

    @Override
    public SleepLog mapRow(ResultSet rs, int rowNum) throws SQLException {
        var bedTime = rs.getObject("bed_time", LocalTime.class);
        var wakeUpTime = rs.getObject("wake_up_time", LocalTime.class);
        var sleepDate = rs.getObject("sleep_date", LocalDate.class);
        var quality = SleepQuality.valueOf(rs.getString("quality"));

        var sleepLogBuilder = SleepLog.builder()
                .wakeUpTime(sleepDate.atTime(wakeUpTime))
                .quality(quality);

        var isBedTimeFromPreviousDay = bedTime.isAfter(wakeUpTime);

        if (isBedTimeFromPreviousDay) {
            sleepLogBuilder.bedTime(sleepDate.minusDays(1).atTime(bedTime));
        } else {
            sleepLogBuilder.bedTime(sleepDate.atTime(bedTime));
        }

        return sleepLogBuilder.build();
    }
}
