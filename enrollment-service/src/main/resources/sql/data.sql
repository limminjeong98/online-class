INSERT INTO payments (payment_id, user_id, payment_type, amount, payment_method, payment_date)
VALUES (1, 1, 'COURSE', 200.00, 'CARD', NOW()),
       (2, 1, 'COURSE', 150.00, 'CARD', NOW()),
       (3, 2, 'COURSE', 200.00, 'CARD', NOW()),
       (4, 2, 'COURSE', 150.00, 'CARD', NOW()),
       (5, 1, 'SUBSCRIPTION', 120.00, 'CARD', NOW()),
       (6, 2, 'SUBSCRIPTION', 180.00, 'CARD', NOW());

INSERT INTO enrollments (enrollment_id, user_id, course_id, payment_id, registration_date)
VALUES (1, 1, 1, 1, NOW()),
       (2, 1, 2, 2, NOW()),
       (3, 2, 1, 3, NOW()),
       (4, 2, 2, 4, NOW());


INSERT INTO subscriptions (subscription_id, user_id, payment_id, start_date, end_date)
VALUES (1, 1, 5, '2024-05-01 00:00:00', '2024-11-01 00:00:00'),
       (2, 2, 6, '2024-05-02 00:00:00', '2024-12-01 00:00:00');