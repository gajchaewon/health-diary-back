package com.bodytok.healthdiary.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;


@Getter
@Table(name = "personal_exercise_diary_hashtag")
@Entity
public class PersonalExerciseDiaryHashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_hashtag_id")
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private PersonalExerciseDiary personalExerciseDiary;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hashtag_id")
    private Hashtag hashtag;

    protected PersonalExerciseDiaryHashtag() {}

    private PersonalExerciseDiaryHashtag(PersonalExerciseDiary personalExerciseDiary, Hashtag hashtag) {
        this.personalExerciseDiary = personalExerciseDiary;
        this.hashtag = hashtag;
    }

    public static PersonalExerciseDiaryHashtag of(PersonalExerciseDiary personalExerciseDiary, Hashtag hashtag) {
        return new PersonalExerciseDiaryHashtag(personalExerciseDiary,hashtag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!( o instanceof PersonalExerciseDiaryHashtag that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}