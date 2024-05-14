package com.bodytok.healthdiary.domain;


import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Table(indexes = {
        @Index(columnList = "createdAt")
})
@Entity
public class Routine extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "routine_id", nullable = false)
    private Long id;

    @Column(nullable = false)
    private String routineName;

    @Column
    private String memo;

    @Setter
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount userAccount;

    @OneToMany(mappedBy = "routine", cascade = CascadeType.REMOVE)
    private List<ExerciseRoutine> exerciseRoutines = new ArrayList<>();

    protected Routine() {
    }

    private Routine(String routineName, String memo) {
    }

    public static Routine of(String routineName, String memo) {
        return new Routine(routineName, memo);
    }

    /* 연관관계 메소드 */
    public void addExercise(ExerciseRoutine exerciseRoutine) {
        exerciseRoutines.add(exerciseRoutine);
    }

}
