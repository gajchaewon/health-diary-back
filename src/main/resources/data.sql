
-- UserAccount 테이블에 대한 테스트 데이터
INSERT INTO user_account (user_id, email, user_password, nickname, created_at, modified_at)
VALUES (1,'test1@gmail.com', 'password1', 'nickname1', NOW(), NOW()),
       (2,'test2@gmail.com', 'password2', 'nickname2', NOW(), NOW());

-- PersonalExerciseDiary 테이블에 대한 테스트 데이터
INSERT INTO personal_exercise_diary (diary_id, user_id, title, content, total_ex_time, is_public, youtube_url, created_at,
                                   modified_at)
VALUES (1, 1, 'title1', 'content1', 30, true, 'youtubeUrl1', NOW(), NOW()),
       (2, 2, 'title2', 'content2', 60, false, 'youtubeUrl2', NOW(), NOW()),
       (3, 1, 'title3', 'content3', 45, true, 'youtubeUrl3', NOW(), NOW()),
       (4, 1, 'title4', 'content4', 90, false, 'youtubeUrl4', NOW(), NOW()),
       (5, 1, 'title5', 'content5', 120, true, 'youtubeUrl5', NOW(), NOW()),
       (6, 2, 'title6', 'content6', 150, false, 'youtubeUrl6', NOW(), NOW()),
       (7, 2, 'title7', 'content7', 180, true, 'youtubeUrl7', NOW(), NOW()),
       (8, 2, 'title8', 'content8', 210, false, 'youtubeUrl8', NOW(), NOW()),
       (9, 2, 'title9', 'content9', 240, true, 'youtubeUrl9', NOW(), NOW()),
       (10, 2, 'title10', 'content10', 270, false, 'youtubeUrl10', NOW(), NOW());

INSERT INTO hashtag(hashtag_id, hashtag) VALUES (1, 'test1');
INSERT INTO hashtag(hashtag_id, hashtag) VALUES (2, 'test2');
INSERT INTO hashtag(hashtag_id, hashtag) VALUES (3, 'test3');
INSERT INTO hashtag(hashtag_id, hashtag) VALUES (4, 'test4');
INSERT INTO personal_exercise_diary_hashtag(diary_id, hashtag_id)
VALUES (1,1);
INSERT INTO personal_exercise_diary_hashtag(diary_id, hashtag_id)
VALUES (2,2);
INSERT INTO personal_exercise_diary_hashtag(diary_id, hashtag_id)
VALUES (3,3);
INSERT INTO personal_exercise_diary_hashtag(diary_id, hashtag_id)
VALUES (4,4);