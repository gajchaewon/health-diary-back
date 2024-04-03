package com.bodytok.healthdiary.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "title"),
        @Index(columnList = "createdAt")
})
@Entity
public class PersonalExerciseDiary extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diary_id")
    private Long id;

    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount userAccount;

    @Setter
    @Column(nullable = false, length = 100)
    private String title;

    @Setter
    @Column(nullable = false)
    private String content;

    @Setter
    @ColumnDefault("0")
    private Integer totalExTime;

    @Setter
    @ColumnDefault("false")
    @Column(nullable = false)
    private Boolean isPublic;

    @ToString.Exclude
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "personalExerciseDiary", cascade = CascadeType.ALL)
    private Set<Comment> comments = new LinkedHashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "personalExerciseDiary", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PersonalExerciseDiaryHashtag> diaryHashtags = new HashSet<>();

    // 좋아요 필드 추가
    @ToString.Exclude
    @OneToMany(mappedBy = "personalExerciseDiary", cascade = CascadeType.ALL, orphanRemoval = true)
    private  Set<DiaryLike> likes = new LinkedHashSet<>();

    @ToString.Exclude
    @OneToMany(mappedBy = "personalExerciseDiary", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<DiaryImage> diaryImages = new HashSet<>();

    public void addDiaryImage(DiaryImage diaryImage) {
        diaryImages.add(diaryImage);
        diaryImage.setPersonalExerciseDiary(this);
    }

    public void removeDiaryImage(DiaryImage diaryImage) {
        diaryImages.remove(diaryImage);
        diaryImage.setPersonalExerciseDiary(null);
    }

    protected PersonalExerciseDiary() {
    }

    private PersonalExerciseDiary(UserAccount userAccount, String title, String content, Integer totalExTime, Boolean isPublic) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
        this.totalExTime = totalExTime;
        this.isPublic = isPublic;
    }

    public static PersonalExerciseDiary of(UserAccount userAccount, String title, String content, Integer totalExTime, Boolean isPublic) {
        return new PersonalExerciseDiary(userAccount, title, content, totalExTime, isPublic);
    }


    // 해시태그를 추가하는 메서드
    public void addHashtag(Hashtag hashtag) {
        var diaryHashtag = PersonalExerciseDiaryHashtag.of(this, hashtag);
        diaryHashtags.add(diaryHashtag);
    }

    // 해시태그를 제거하는 메서드
    public void removeHashtag(Hashtag hashtag) {
        for (PersonalExerciseDiaryHashtag diaryHashtag : diaryHashtags) {
            if (diaryHashtag.getPersonalExerciseDiary().equals(this) &&
                    diaryHashtag.getHashtag().equals(hashtag)) {
                diaryHashtags.remove(diaryHashtag);
                diaryHashtag.setPersonalExerciseDiary(null);
                diaryHashtag.setHashtag(null);
                return;
            }
        }
    }

    public void addLike(DiaryLike like) {
        likes.add(like);
    }
    public void removeLike(DiaryLike like){
        likes.remove(like);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonalExerciseDiary that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
