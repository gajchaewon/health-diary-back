package com.bodytok.healthdiary.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;


@Getter
@Entity
public class Follow extends AuditingFields {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id", nullable = false)
    private UserAccount follower;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id", nullable = false)
    private UserAccount following;


    protected Follow(){}

    private Follow(UserAccount follower, UserAccount following) {
        this.follower = follower;
        this.following = following;
    }

    public static Follow of(UserAccount follower, UserAccount following){
        return new Follow(follower, following);
    }


}
