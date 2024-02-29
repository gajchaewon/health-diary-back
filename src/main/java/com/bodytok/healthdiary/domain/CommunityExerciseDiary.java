package com.bodytok.healthdiary.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString
@Entity
public class CommunityExerciseDiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_diary_id")
    private Long id;

    @Setter
    @OneToOne(optional = false)
    @JoinColumn(name = "diary_id", nullable = false)
    private PersonalExerciseDiary personalExerciseDiary;


    protected CommunityExerciseDiary() {
    }

    private CommunityExerciseDiary(PersonalExerciseDiary personalExerciseDiary) {
        this.personalExerciseDiary = personalExerciseDiary;
    }

    public static CommunityExerciseDiary of(PersonalExerciseDiary personalExerciseDiary) {
        return new CommunityExerciseDiary(personalExerciseDiary);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommunityExerciseDiary that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
