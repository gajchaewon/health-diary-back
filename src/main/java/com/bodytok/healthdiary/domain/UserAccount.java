package com.bodytok.healthdiary.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;

@Getter
@ToString(callSuper = true)
@Table(indexes = {
        @Index(columnList = "email", unique = true),
        @Index(columnList = "createdAt")
})
@Entity
public class UserAccount extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Setter
    @Column(nullable = false, length = 100)
    private String email;

    @Setter
    @Column(unique = true, length = 100)
    private String nickname;

    @Setter
    @Column(nullable = false)
    private String userPassword;

    @Setter
    @OneToOne(mappedBy = "userAccount", cascade = CascadeType.ALL, orphanRemoval = true)
    private ProfileImage profileImage;


    //팔로우
    @ToString.Exclude
    @OneToMany(mappedBy = "following")
    private List<Follow> followerList;

    @ToString.Exclude
    @OneToMany(mappedBy = "follower")
    private List<Follow> followingList;

    protected UserAccount() {}

    @Builder
    private UserAccount(Long id, String email,String nickname, String userPassword, ProfileImage profileImage) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.userPassword = userPassword;
        this.profileImage = profileImage;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAccount that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}