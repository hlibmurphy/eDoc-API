insert into appointments (id, date, is_deleted, is_online, start_time, end_time, doctor_id, user_id)
values (1, CURRENT_DATE + INTERVAL '1' DAY, false, false, '09:00:00', '10:00:00', 1, 1);