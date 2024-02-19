
-- UserAccount 테이블에 대한 테스트 데이터
INSERT INTO user_account (user_id, email, user_password, nickname, created_at, modified_at)
VALUES (1,'john@gmail.com', '$2a$12$K20J0MRQCGDodS9lQLcKSe8C0nypzDqhoab3mJ95gBRSYbRVlkazO', 'John', NOW(), NOW()),
       (2,'kevin@gmail.com', '$2a$12$rp2n26LK9YOY22aFN9F50.smMa3oJVxR9tLwmNAT1s4eqkTDGofqK', 'Kevin', NOW(), NOW());

-- PersonalExerciseDiary 테이블에 대한 테스트 데이터
INSERT INTO personal_exercise_diary (diary_id, user_id, title, content, total_ex_time, is_public, created_at,
                                   modified_at)
VALUES (1, 1, 'title1', 'content1', 30, true,  NOW(), NOW()),
       (2, 2, 'title2', 'content2', 60, false,  NOW(), NOW()),
       (3, 1, 'title3', 'content3', 45, true,  NOW(), NOW()),
       (4, 1, 'title4', 'content4', 90, false,  NOW(), NOW()),
       (5, 1, 'title5', 'content5', 120, true,  NOW(), NOW()),
       (6, 2, 'title6', 'content6', 150, false, NOW(), NOW()),
       (7, 2, 'title7', 'content7', 180, true,  NOW(), NOW()),
       (8, 2, 'title8', 'content8', 210, false,  NOW(), NOW()),
       (9, 2, 'title9', 'content9', 240, true, NOW(), NOW()),
       (10, 2, 'title10', 'content10', 270, false,  NOW(), NOW());

INSERT INTO comment (comment_id, created_at, diary_id, modified_at, user_id, content)
VALUES (1, NOW(), 1, NOW(), 1, '유저 John 의 테스트 댓글 - title1'),
       (2, NOW(), 1, NOW(), 1, '유저 John 의 테스트 댓글 - title1'),
       (3, NOW(), 1, NOW(), 1, '유저 John 의 테스트 댓글 - title1'),
       (4, NOW(), 3, NOW(), 1, '유저 John 의 테스트 댓글 - title3'),
       (5, NOW(), 7, NOW(), 1, '유저 John 의 테스트 댓글 - title7');



INSERT INTO hashtag(hashtag_id, hashtag) VALUES (1, 'first');
INSERT INTO hashtag(hashtag_id, hashtag) VALUES (2, 'first first');
INSERT INTO hashtag(hashtag_id, hashtag) VALUES (3, 'new');
INSERT INTO hashtag(hashtag_id, hashtag) VALUES (4, 'newnew');
INSERT INTO hashtag(hashtag_id, hashtag) VALUES (5, 'unique');
INSERT INTO hashtag(hashtag_id, hashtag) VALUES (6, 'cccceedasdwq');
INSERT INTO hashtag(hashtag_id, hashtag) VALUES (7, 'want some coffee');
INSERT INTO hashtag(hashtag_id, hashtag) VALUES (8, 'latte');
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