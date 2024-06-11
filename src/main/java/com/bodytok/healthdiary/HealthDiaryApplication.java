package com.bodytok.healthdiary;

import com.bodytok.healthdiary.repository.jwt.JwtTokenRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

//@EnableJpaRepositories(basePackageClasses = {
//		CommentRepository.class, DiaryImageRepository.class, DiaryLikeRepository.class, ExerciseTimeLogRepository.class,
//		FollowRepository.class, HashtagRepository.class, PersonalExerciseDiaryHashtagRepository.class, PersonalExerciseDiaryRepository.class,
//		UserAccountRepository.class
//})
@EnableJpaRepositories(basePackages = "com.bodytok.healthdiary.repository", excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = JwtTokenRepository.class))
@SpringBootApplication
public class HealthDiaryApplication {

	public static void main(String[] args) {
		SpringApplication.run(HealthDiaryApplication.class, args);
	}

}
