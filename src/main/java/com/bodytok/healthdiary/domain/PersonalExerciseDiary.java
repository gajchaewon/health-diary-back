package com.bodytok.healthdiary.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;

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

    @Setter
    @Column()
    private String youtubeUrl;

    //diary 에서 댓글 불러올 일이 많으므로 양방향 연결함
    @ToString.Exclude /*-> circular referential 발생 방지*/
    @OrderBy("createdAt DESC")
    @OneToMany(mappedBy = "personalExerciseDiary", cascade = CascadeType.ALL)
    private final Set<Comment> comments  = new LinkedHashSet<>();

    //해시태그 또한 다이어리에서 많이 조회하므로 양방향 연결
    @ToString.Exclude
    @OrderBy("hashtag DESC")
    @OneToMany(mappedBy = "personalExerciseDiary",  cascade = CascadeType.ALL)
    private final Set<PersonalExerciseDiaryHashtag> diaryHashtags = new LinkedHashSet<>();

    protected PersonalExerciseDiary() {
    }

    private PersonalExerciseDiary(UserAccount userAccount, String title, String content, Integer totalExTime, Boolean isPublic, String youtubeUrl) {
        this.userAccount = userAccount;
        this.title = title;
        this.content = content;
        this.totalExTime = totalExTime;
        this.isPublic = isPublic;
        this.youtubeUrl = youtubeUrl;
    }

    public  static PersonalExerciseDiary of(UserAccount userAccount, String title, String content, Integer totalExTime, Boolean isPublic, String youtubeUrl) {
        return new PersonalExerciseDiary(userAccount, title, content, totalExTime, isPublic, youtubeUrl);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PersonalExerciseDiary that)) return false; // pattern variable
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
