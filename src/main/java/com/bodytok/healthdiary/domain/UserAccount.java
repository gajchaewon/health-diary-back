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
    @Lob
    private Byte[] profileImage;


    //팔로우
    @ToString.Exclude
    @OneToMany(mappedBy = "following")
    private List<Follow> followerList;

    @ToString.Exclude
    @OneToMany(mappedBy = "follower")
    private List<Follow> followingList;

    protected UserAccount() {}

    @Builder
    private UserAccount(Long id, String email,String nickname, String userPassword, Byte[] profileImage) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.userPassword = userPassword;
        this.profileImage = profileImage;
    }

    public static UserAccount of(String email,String nickname, String userPassword, Byte[] profileImage) {
        return UserAccount.builder()
                .email(email).nickname(nickname).userPassword(userPassword).profileImage(profileImage)
                .build();
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