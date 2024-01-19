package com.bodytok.healthdiary.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;


@Getter
@ToString
@Entity
public class ExerciseTimeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ex_time_id")
    private Long id;

    @Setter
    @ManyToOne
    @JoinColumn(name = "diary_id", nullable = false)
    private PersonalExerciseDiary personalExerciseDiary;

    @Setter
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startTime;

    @Setter
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endTime;


}