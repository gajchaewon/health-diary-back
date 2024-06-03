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
    @JoinColumn(name="routine_id")
    private Routine routine;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name="exercise_id")
    private Exercise exercise;

    protected ExerciseRoutine() {}

    private ExerciseRoutine(Routine routine,Exercise exercise) {
        setExercise(exercise);
        setRoutine(routine);
    }

    public static ExerciseRoutine of(Routine routine, Exercise exercise) {
        return new ExerciseRoutine(routine, exercise);
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
