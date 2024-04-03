package com.bodytok.healthdiary.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;


@Getter
@Table(indexes = {
        @Index(columnList = "createdAt"),
})
@Entity
public class DiaryLike extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount userAccount;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id", nullable = false)
    private PersonalExerciseDiary personalExerciseDiary;

    protected DiaryLike() {
    }

    private DiaryLike(UserAccount userAccount, PersonalExerciseDiary personalExerciseDiary) {
        this.userAccount = userAccount;
        this.personalExerciseDiary = personalExerciseDiary;
    }

    public static DiaryLike of(UserAccount userAccount, PersonalExerciseDiary personalExerciseDiary) {
        return new DiaryLike(userAccount, personalExerciseDiary);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiaryLike that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}