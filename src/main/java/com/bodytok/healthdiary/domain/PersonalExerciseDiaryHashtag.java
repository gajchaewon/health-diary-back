package com.bodytok.healthdiary.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Table(name = "personal_exercise_diary_hashtag")
@Entity
public class PersonalExerciseDiaryHashtag {

    @EmbeddedId // 복합키로 embedded 된 하나의 id
    private PersonalExerciseDiaryHashtagId id;

    @MapsId("diaryId")
    @Setter
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "diary_id", referencedColumnName = "diary_id")
    private PersonalExerciseDiary personalExerciseDiary;


    @MapsId("hashtagId")
    @Setter
    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "hashtag_id", referencedColumnName = "hashtag_id")
    private Hashtag hashtag;

    protected PersonalExerciseDiaryHashtag() {}

    private PersonalExerciseDiaryHashtag(PersonalExerciseDiary diary, Hashtag hashtag) {
        this.personalExerciseDiary = diary;
        this.hashtag = hashtag;
        this.id = PersonalExerciseDiaryHashtagId.of(diary.getId(), hashtag.getId());
    }

    public static PersonalExerciseDiaryHashtag of(PersonalExerciseDiary diary, Hashtag hashtag){
        return new PersonalExerciseDiaryHashtag(diary,hashtag);
    }


}