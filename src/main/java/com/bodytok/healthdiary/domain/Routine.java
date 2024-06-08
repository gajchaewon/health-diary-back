package com.bodytok.healthdiary.domain;


import com.bodytok.healthdiary.dto.exercise_routine.request.RoutineUpdate;
import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
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
    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private List<Exercise> exercises = new ArrayList<>();

    @Setter
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount userAccount;

    protected Routine() {
    }

    private Routine(String routineName, String memo, UserAccount userAccount) {
        this.routineName = routineName;
        this.memo = memo;
        this.userAccount = userAccount;
    }

    public void addExercise(Exercise exercise) {
        exercises.add(exercise);
    }
    public void removeExercise(String exerciseId){
        exercises.removeIf(exercise -> Objects.equals(exercise.getId(), exerciseId));
    }

    public void updateRoutineInfo(RoutineUpdate routineUpdate) {
        this.routineName = routineUpdate.routineName();
        this.memo = routineUpdate.memo();
    }


    public static Routine of(String routineName, String memo, UserAccount userAccount) {
        return new Routine(routineName, memo, userAccount);
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
