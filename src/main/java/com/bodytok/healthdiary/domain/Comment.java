package com.bodytok.healthdiary.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "content"),
        @Index(columnList = "createdAt")
})
@Entity
public class Comment extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Setter
    @Column(nullable = false, length = 500)
    private String content;

    @ToString.Exclude
    @Setter
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount userAccount;

    @ToString.Exclude
    @Setter
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id", nullable = false)
    private PersonalExerciseDiary personalExerciseDiary;


    protected Comment() {
    }

    private Comment(UserAccount userAccount, PersonalExerciseDiary personalExerciseDiary, String content) {
        this.userAccount = userAccount;
        this.personalExerciseDiary = personalExerciseDiary;
        this.content = content;
    }

    public static Comment of(UserAccount userAccount, PersonalExerciseDiary personalExerciseDiary, String content) {
        return new Comment(userAccount, personalExerciseDiary, content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!( o instanceof Comment that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
