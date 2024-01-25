package com.bodytok.healthdiary.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class PersonalExerciseDiaryHashtagId implements Serializable { // 복합키 클래스 (Embeddable) 은 Serializable 을 구현해야함

    @Column(name = "diary_id")
    private Long diaryId;

    @Column(name = "hashtag_id")
    private Long hashtagId;

    protected PersonalExerciseDiaryHashtagId() {
    }

    private PersonalExerciseDiaryHashtagId(Long diaryId, Long hashtagId) {
        this.diaryId = diaryId;
        this.hashtagId = hashtagId;
    }

    public static PersonalExerciseDiaryHashtagId of(Long diaryId, Long hashtagId) {
        return new PersonalExerciseDiaryHashtagId(diaryId, hashtagId);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PersonalExerciseDiaryHashtagId that = (PersonalExerciseDiaryHashtagId) o;
        return Objects.equals(diaryId, that.diaryId) &&
                Objects.equals(hashtagId, that.hashtagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(diaryId, hashtagId);
    }

}