CREATE TABLE sleep_logs (
    id SERIAL PRIMARY KEY,
    user_id  UUID NOT NULL,
    sleep_date DATE NOT NULL,
    bed_time TIME NOT NULL,
    wake_up_time TIME NOT NULL,
    quality VARCHAR(4) NOT NULL
);