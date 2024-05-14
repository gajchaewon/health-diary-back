package com.bodytok.healthdiary.domain;


import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Table(name = "exercise_routine")
@Entity
public class ExerciseRoutine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_routine_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="exercise_id")
    private Exercise exercise;


    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="routine_id")
    private Routine routine;

    protected ExerciseRoutine() {}

    private ExerciseRoutine(Exercise exercise, Routine routine) {
        setExercise(exercise);
        setRoutine(routine);
    }

    public static ExerciseRoutine of(Exercise exercise, Routine routine) {
        return new ExerciseRoutine(exercise, routine);
    }

    //setter 로 강제시킴
    private void setExercise(Exercise exercise) {
        this.exercise = exercise;
        exercise.addRoutine(this);
    }

    private void setRoutine(Routine routine) {
        this.routine = routine;
        routine.addExercise(this);
    }

}
