package com.bodytok.healthdiary.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;


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

    private PersonalExerciseDiaryHashtag(PersonalExerciseDiaryHashtagId diaryHashtagId, PersonalExerciseDiary diary, Hashtag hashtag) {
        this.id = diaryHashtagId;
        this.personalExerciseDiary = diary;
        this.hashtag = hashtag;
    }

    public static PersonalExerciseDiaryHashtag of(PersonalExerciseDiaryHashtagId diaryHashtagId, PersonalExerciseDiary diary, Hashtag hashtag){
        return new PersonalExerciseDiaryHashtag(diaryHashtagId, diary, hashtag);
    }

    public static PersonalExerciseDiaryHashtag of(PersonalExerciseDiary diary, Hashtag hashtag){
        return new PersonalExerciseDiaryHashtag(
                PersonalExerciseDiaryHashtagId.of(diary.getId(), hashtag.getId()), diary, hashtag
        );
    }


}