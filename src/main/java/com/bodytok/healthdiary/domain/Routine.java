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

    private Routine(String routineName, String memo, UserAccount userAccount) {
        this.routineName = routineName;
        this.memo = memo;
        this.userAccount = userAccount;
    }

    public static Routine of(String routineName, String memo, UserAccount userAccount) {
        return new Routine(routineName, memo, userAccount);
    }

    /* 연관관계 메소드 */
    public void addExercise(ExerciseRoutine exerciseRoutine) {
        exerciseRoutines.add(exerciseRoutine);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Routine that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
