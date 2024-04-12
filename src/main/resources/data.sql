
-- UserAccount 테이블에 대한 테스트 데이터 / root 비밀번호 root, 나머지 유저 비밀번호 password1234!
INSERT INTO user_account (user_id, email, user_password, nickname, created_at, modified_at)
VALUES (1,'john@gmail.com', '$2a$12$J.sy3QYAPoi5NqiKCF215uEd.XJNnkRV78GDGdi2aRQtlKNcVr2Oi', 'John',DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY)),
       (2,'kevin@gmail.com', '$2a$12$J.sy3QYAPoi5NqiKCF215uEd.XJNnkRV78GDGdi2aRQtlKNcVr2Oi', 'Kevin', DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY)),
       (3,'root@mail.com','$2a$12$V3XOFkOKSgBpHvsChiN.0uS/RBweBCK1CPWrgqVcKJutuXf.C.0kq','root',DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY)),
       (4, 'emma@gmail.com', '$2a$12$J.sy3QYAPoi5NqiKCF215uEd.XJNnkRV78GDGdi2aRQtlKNcVr2Oi', 'Emma', DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY)),
       (5, 'lisa@gmail.com', '$2a$12$J.sy3QYAPoi5NqiKCF215uEd.XJNnkRV78GDGdi2aRQtlKNcVr2Oi', 'Lisa', DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY)),
       (6, 'alex@gmail.com', '$2a$12$J.sy3QYAPoi5NqiKCF215uEd.XJNnkRV78GDGdi2aRQtlKNcVr2Oi', 'Alex', DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY)),
       (7, 'sophia@gmail.com', '$2a$12$J.sy3QYAPoi5NqiKCF215uEd.XJNnkRV78GDGdi2aRQtlKNcVr2Oi', 'Sophia', DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY)),
       (8, 'james@gmail.com', '$2a$12$J.sy3QYAPoi5NqiKCF215uEd.XJNnkRV78GDGdi2aRQtlKNcVr2Oi', 'James', DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY)),
       (9, 'sarah@gmail.com', '$2a$12$J.sy3QYAPoi5NqiKCF215uEd.XJNnkRV78GDGdi2aRQtlKNcVr2Oi', 'Sarah', DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY)),
       (10, 'michael@gmail.com', '$2a$12$J.sy3QYAPoi5NqiKCF215uEd.XJNnkRV78GDGdi2aRQtlKNcVr2Oi', 'Michael', DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY)),
       (11, 'olivia@gmail.com', '$2a$12$J.sy3QYAPoi5NqiKCF215uEd.XJNnkRV78GDGdi2aRQtlKNcVr2Oi', 'Olivia', DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY)),
       (12, 'david@gmail.com', '$2a$12$J.sy3QYAPoi5NqiKCF215uEd.XJNnkRV78GDGdi2aRQtlKNcVr2Oi', 'David', DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY)),
       (13, 'emily@gmail.com', '$2a$12$J.sy3QYAPoi5NqiKCF215uEd.XJNnkRV78GDGdi2aRQtlKNcVr2Oi', 'Emily', DATE_SUB(NOW(), INTERVAL 30 DAY), DATE_SUB(NOW(), INTERVAL 30 DAY));

-- PersonalExerciseDiary 테이블에 대한 테스트 데이터
INSERT INTO personal_exercise_diary (diary_id, user_id, title, content, total_ex_time, is_public, created_at,
                                   modified_at)
VALUES (1, 1, 'title1', 'content1', 30, true,  NOW(), NOW()),
       (2, 2, 'title2', 'content2', 60, false,  NOW(), NOW()),
       (3, 1, 'title3', 'content3', 45, true,  NOW(), NOW()),
       (4, 1, 'title4', 'content4', 90, false,  NOW(), NOW()),
       (5, 3, 'title5', 'content5', 120, true,  NOW(), NOW()),
       (6, 3, 'title6', 'content6', 150, false, NOW(), NOW()),
       (7, 3, 'title7', 'content7', 180, true,  NOW(), NOW()),
       (8, 2, 'title8', 'content8', 210, false,  NOW(), NOW()),
       (9, 5, 'title9', 'content9', 240, true, NOW(), NOW()),
       (10, 5, 'title10', 'content10', 270, false,  NOW(), NOW()),
       (11, 11, 'title11', 'content11', 30, true, DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY)),
       (12, 11, 'title12', 'content12', 45, false, DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY)),
       (13, 6, 'title13', 'content13', 60, true, DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY)),
       (14, 7, 'title14', 'content14', 90, false, DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY)),
       (15, 7, 'title15', 'content15', 120, true, DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY)),
       (16, 12, 'title16', 'content16', 150, false, DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY)),
       (17, 12, 'title17', 'content17', 180, true, DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY)),
       (18, 12, 'title18', 'content18', 210, false, DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY)),
       (19, 10, 'title19', 'content19', 240, true, DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY)),
       (20, 10, 'title20', 'content20', 270, false, DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY)),
       (21, 1, 'title21', 'content21', 300, true, DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY)),
       (22, 8, 'title22', 'content22', 330, false, DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY)),
       (23, 8, 'title23', 'content23', 360, true, DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY)),
       (24, 8, 'title24', 'content24', 390, false, DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY)),
       (25, 9, 'title25', 'content25', 420, true, DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY)),
       (26, 9, 'title26', 'content26', 450, false, DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY)),
       (27, 9, 'title27', 'content27', 480, true, DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY)),
       (28, 4, 'title28', 'content28', 510, false, DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY)),
       (29, 4, 'title29', 'content29', 540, true, DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY)),
       (30, 4, 'title30', 'content30', 570, false, DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY), DATE_SUB(NOW(), INTERVAL FLOOR(RAND() * 30) DAY));


INSERT INTO comment (comment_id, created_at, diary_id, modified_at, user_id, content)
SELECT
        (SELECT MAX(comment_id) FROM comment) + ROW_NUMBER() OVER (ORDER BY RAND()) AS comment_id,
        DATE_ADD(p.created_at, INTERVAL FLOOR(RAND() * 30) DAY) AS created_at,
        p.diary_id,
        NOW() AS modified_at,
        u.user_id,
        CONCAT('Comment ', ROW_NUMBER() OVER (ORDER BY RAND())) AS content
FROM
    personal_exercise_diary p
        CROSS JOIN
    user_account u
WHERE
        p.created_at <= NOW()
ORDER BY
    RAND()
LIMIT 20;


INSERT INTO hashtag(hashtag_id, hashtag) VALUES (1, 'first');
INSERT INTO hashtag(hashtag_id, hashtag) VALUES (2, 'first first');
INSERT INTO hashtag(hashtag_id, hashtag) VALUES (3, 'new');
INSERT INTO hashtag(hashtag_id, hashtag) VALUES (4, 'newnew');
INSERT INTO hashtag(hashtag_id, hashtag) VALUES (5, 'unique');
INSERT INTO hashtag(hashtag_id, hashtag) VALUES (6, 'cccceedasdwq');
INSERT INTO hashtag(hashtag_id, hashtag) VALUES (7, 'want some coffee');
INSERT INTO hashtag(hashtag_id, hashtag) VALUES (8, 'latte');
INSERT INTO hashtag(hashtag) VALUES ('newhashtag');
INSERT INTO personal_exercise_diary_hashtag(diary_id, hashtag_id)
VALUES (1,1);
INSERT INTO personal_exercise_diary_hashtag(diary_id, hashtag_id)
VALUES (2,2);
INSERT INTO personal_exercise_diary_hashtag(diary_id, hashtag_id)
VALUES (3,3);
INSERT INTO personal_exercise_diary_hashtag(diary_id, hashtag_id)
VALUES (4,4);
INSERT INTO personal_exercise_diary_hashtag(diary_id, hashtag_id)
VALUES (1,5);
INSERT INTO personal_exercise_diary_hashtag(diary_id, hashtag_id)
VALUES (2,6);
INSERT INTO personal_exercise_diary_hashtag(diary_id, hashtag_id)
VALUES (3,7);
INSERT INTO personal_exercise_diary_hashtag(diary_id, hashtag_id)
VALUES (4,8);