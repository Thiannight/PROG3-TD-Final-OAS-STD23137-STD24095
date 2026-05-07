-- ============================================================
-- DONNÉES DE TEST BONUS - Activités & Présences
-- ============================================================

-- ============================================================
-- 1) ACTIVITÉS
-- ============================================================

-- Collectivité 1
INSERT INTO collectivity_activity (id, collectivity_id, label, activity_type, member_occupation_concerned,
                                   recurrence_week_ordinal, recurrence_day_of_week, executive_date)
VALUES ('act-1', 'col-1', 'AG1', 'MEETING', 'JUNIOR,SENIOR,SECRETARY,TREASURER,VICE_PRESIDENT,PRESIDENT', 1, 'SA',
        NULL),
       ('act-2', 'col-1', 'Formation de base', 'TRAINING', 'JUNIOR', 2, 'SU', NULL);

-- Collectivité 2
INSERT INTO collectivity_activity (id, collectivity_id, label, activity_type, member_occupation_concerned,
                                   recurrence_week_ordinal, recurrence_day_of_week, executive_date)
VALUES ('act-3', 'col-2', 'AG2', 'MEETING', 'JUNIOR,SENIOR,SECRETARY,TREASURER,VICE_PRESIDENT,PRESIDENT', 1, 'SU',
        NULL),
       ('act-4', 'col-2', 'Formation de base', 'TRAINING', 'JUNIOR', 3, 'SU', NULL),
       ('act-5', 'col-2', 'Perfectionnement', 'PUNCTUAL', 'SENIOR', NULL, NULL, '3036-04-30');

-- Collectivité 3
INSERT INTO collectivity_activity (id, collectivity_id, label, activity_type, member_occupation_concerned,
                                   recurrence_week_ordinal, recurrence_day_of_week, executive_date)
VALUES ('act-6', 'col-3', 'AG3', 'MEETING', 'JUNIOR,SENIOR,SECRETARY,TREASURER,VICE_PRESIDENT,PRESIDENT', 1, 'FR',
        NULL),
       ('act-7', 'col-3', 'Formation de base', 'TRAINING', 'JUNIOR', 4, 'WE', NULL);


-- ============================================================
-- 2) PRÉSENCES
-- ============================================================

-- Collectivité 1 – act-1 (AG1) – Mars 2026 (07/03/2026)
INSERT INTO activity_attendance (id, activity_id, member_id, attendance_status, activity_date)
VALUES ('att-1-1', 'act-1', 'C1-M1', 'ATTENDED', '2026-03-07'),
       ('att-1-2', 'act-1', 'C1-M2', 'ATTENDED', '2026-03-07'),
       ('att-1-3', 'act-1', 'C1-M3', 'ATTENDED', '2026-03-07'),
       ('att-1-4', 'act-1', 'C1-M4', 'ATTENDED', '2026-03-07'),
       ('att-1-5', 'act-1', 'C1-M5', 'ATTENDED', '2026-03-07'),
       ('att-1-6', 'act-1', 'C1-M6', 'ATTENDED', '2026-03-07'),
       ('att-1-7', 'act-1', 'C1-M7', 'MISSING', '2026-03-07'),
       ('att-1-8', 'act-1', 'C1-M8', 'MISSING', '2026-03-07');

-- Collectivité 1 – act-1 (AG1) – Avril 2026 (04/04/2026)
INSERT INTO activity_attendance (id, activity_id, member_id, attendance_status, activity_date)
VALUES ('att-2-1', 'act-1', 'C1-M1', 'ATTENDED', '2026-04-04'),
       ('att-2-2', 'act-1', 'C1-M2', 'ATTENDED', '2026-04-04'),
       ('att-2-3', 'act-1', 'C1-M3', 'MISSING', '2026-04-04'),
       ('att-2-4', 'act-1', 'C1-M4', 'MISSING', '2026-04-04'),
       ('att-2-5', 'act-1', 'C1-M5', 'ATTENDED', '2026-04-04'),
       ('att-2-6', 'act-1', 'C1-M6', 'ATTENDED', '2026-04-04'),
       ('att-2-7', 'act-1', 'C1-M7', 'ATTENDED', '2026-04-04'),
       ('att-2-8', 'act-1', 'C1-M8', 'ATTENDED', '2026-04-04');

-- Collectivité 2 – act-3 (AG2) – Mars 2026 (08/03/2026)
INSERT INTO activity_attendance (id, activity_id, member_id, attendance_status, activity_date)
VALUES ('att-3-1', 'act-3', 'C1-M1', 'ATTENDED', '2026-03-08'),
       ('att-3-2', 'act-3', 'C1-M2', 'ATTENDED', '2026-03-08'),
       ('att-3-3', 'act-3', 'C1-M3', 'MISSING', '2026-03-08'),
       ('att-3-4', 'act-3', 'C1-M4', 'MISSING', '2026-03-08'),
       ('att-3-5', 'act-3', 'C1-M5', 'ATTENDED', '2026-03-08'),
       ('att-3-6', 'act-3', 'C1-M6', 'ATTENDED', '2026-03-08'),
       ('att-3-7', 'act-3', 'C1-M7', 'ATTENDED', '2026-03-08'),
       ('att-3-8', 'act-3', 'C1-M8', 'ATTENDED', '2026-03-08');

-- Collectivité 2 – act-3 (AG2) – Avril 2026 (05/04/2026)
INSERT INTO activity_attendance (id, activity_id, member_id, attendance_status, activity_date)
VALUES ('att-4-1', 'act-3', 'C1-M1', 'ATTENDED', '2026-04-05'),
       ('att-4-2', 'act-3', 'C1-M2', 'ATTENDED', '2026-04-05'),
       ('att-4-3', 'act-3', 'C1-M3', 'MISSING', '2026-04-05'),
       ('att-4-4', 'act-3', 'C1-M4', 'ATTENDED', '2026-04-05'),
       ('att-4-5', 'act-3', 'C1-M5', 'ATTENDED', '2026-04-05'),
       ('att-4-6', 'act-3', 'C1-M6', 'ATTENDED', '2026-04-05'),
       ('att-4-7', 'act-3', 'C1-M7', 'ATTENDED', '2026-04-05'),
       ('att-4-8', 'act-3', 'C1-M8', 'MISSING', '2026-04-05');

-- Collectivité 2 – act-5 (Perfectionnement) – 30/04/2026
INSERT INTO activity_attendance (id, activity_id, member_id, attendance_status, activity_date)
VALUES ('att-5-1', 'act-5', 'C1-M1', 'ATTENDED', '2026-04-30'),
       ('att-5-2', 'act-5', 'C1-M2', 'ATTENDED', '2026-04-30'),
       ('att-5-3', 'act-5', 'C1-M3', 'ATTENDED', '2026-04-30'),
       ('att-5-4', 'act-5', 'C1-M4', 'MISSING', '2026-04-30'),
       ('att-5-5', 'act-5', 'C1-M5', 'UNDEFINED', '2026-04-30'),
       ('att-5-6', 'act-5', 'C1-M6', 'UNDEFINED', '2026-04-30'),
       ('att-5-7', 'act-5', 'C1-M7', 'UNDEFINED', '2026-04-30'),
       ('att-5-8', 'act-5', 'C1-M8', 'UNDEFINED', '2026-04-30');

-- Collectivité 3 – act-6 (AG3) – Mars 2026 (06/03/2026)
INSERT INTO activity_attendance (id, activity_id, member_id, attendance_status, activity_date)
VALUES ('att-6-1', 'act-6', 'C3-M1', 'ATTENDED', '2026-03-06'),
       ('att-6-2', 'act-6', 'C3-M2', 'ATTENDED', '2026-03-06'),
       ('att-6-3', 'act-6', 'C3-M3', 'ATTENDED', '2026-03-06'),
       ('att-6-4', 'act-6', 'C3-M4', 'ATTENDED', '2026-03-06'),
       ('att-6-5', 'act-6', 'C3-M5', 'ATTENDED', '2026-03-06'),
       ('att-6-6', 'act-6', 'C3-M6', 'ATTENDED', '2026-03-06'),
       ('att-6-7', 'act-6', 'C3-M7', 'MISSING', '2026-03-06'),
       ('att-6-8', 'act-6', 'C3-M8', 'MISSING', '2026-03-06');

-- Collectivité 3 – act-6 (AG3) – Avril 2026 (03/04/2026)
INSERT INTO activity_attendance (id, activity_id, member_id, attendance_status, activity_date)
VALUES ('att-7-1', 'act-6', 'C3-M1', 'ATTENDED', '2026-04-03'),
       ('att-7-2', 'act-6', 'C3-M2', 'ATTENDED', '2026-04-03'),
       ('att-7-3', 'act-6', 'C3-M3', 'MISSING', '2026-04-03'),
       ('att-7-4', 'act-6', 'C3-M4', 'MISSING', '2026-04-03'),
       ('att-7-5', 'act-6', 'C3-M5', 'ATTENDED', '2026-04-03'),
       ('att-7-6', 'act-6', 'C3-M6', 'ATTENDED', '2026-04-03'),
       ('att-7-7', 'act-6', 'C3-M7', 'MISSING', '2026-04-03'),
       ('att-7-8', 'act-6', 'C3-M8', 'ATTENDED', '2026-04-03'),
       ('att-7-9', 'act-6', 'C1-M1', 'ATTENDED', '2026-04-03');