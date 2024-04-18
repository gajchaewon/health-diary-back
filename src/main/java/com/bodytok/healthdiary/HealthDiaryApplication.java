package com.bodytok.healthdiary;

import com.bodytok.healthdiary.repository.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackageClasses = {
		CommentRepository.class, DiaryImageRepository.class, DiaryLikeRepository.class, ExerciseTimeLogRepository.class,
		FollowRepository.class, HashtagRepository.class, PersonalExerciseDiaryHashtagRepository.class, PersonalExerciseDiaryRepository.class,
		UserAccountRepository.class
})
@SpringBootApplication
public class HealthDiaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthDiaryApplication.class, args);
	}

}
