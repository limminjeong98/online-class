INSERT INTO playback_records (record_id, user_id, file_id, start_time, end_time)
VALUES (1, 1, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (2, 1, 2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
       (3, 2, 3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT INTO event_logs (event_id, record_id, user_id, event_type, timestamp)
VALUES (1, 1, 1, 'play', CURRENT_TIMESTAMP),
       (2, 1, 1, 'pause', CURRENT_TIMESTAMP),
       (3, 1, 1, 'play', CURRENT_TIMESTAMP),
       (4, 1, 1, 'stop', CURRENT_TIMESTAMP),
       (5, 2, 1, 'play', CURRENT_TIMESTAMP),
       (6, 2, 1, 'stop', CURRENT_TIMESTAMP),
       (7, 3, 2, 'play', CURRENT_TIMESTAMP),
       (8, 3, 2, 'pause', CURRENT_TIMESTAMP),
       (9, 3, 2, 'play', CURRENT_TIMESTAMP),
       (10, 3, 2, 'stop', CURRENT_TIMESTAMP);