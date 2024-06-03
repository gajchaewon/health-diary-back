package com.bodytok.healthdiary.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Table(indexes = {
        @Index(columnList = "createdAt")
})
@Entity
public class Exercise extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "exercise_id", nullable = false)
    private Long id;

    @Setter
    @Column(nullable = false)
    private String exerciseName;

    @Setter
    @Column
    private String description;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.REMOVE)
    private List<ExerciseRoutine> exerciseRoutines = new ArrayList<>();

    protected Exercise() {
    }

    private Exercise(String exerciseName, String description) {
        this.exerciseName = exerciseName;
        this.description = description;
    }

    public static Exercise of(String exerciseName, String description) {
        return new Exercise(exerciseName, description);
    }

    /* 연관관계 메소드 */
    public void addRoutine(ExerciseRoutine exerciseRoutine) {
        exerciseRoutines.add(exerciseRoutine);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Exercise that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
