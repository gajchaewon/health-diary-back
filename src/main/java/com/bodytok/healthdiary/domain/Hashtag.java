package com.bodytok.healthdiary.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;


@Getter
@ToString
@Entity
public class Hashtag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hashtag_id")
    private Long id;

    @Setter
    @Column(nullable = false, unique = true)
    private String hashtag;


    protected Hashtag() {}

    private Hashtag(String hashtag) {
        this.hashtag = hashtag;
    }
    public static Hashtag of(String hashtag) {
        return new Hashtag(hashtag);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Hashtag)) return false;
        Hashtag that = (Hashtag) o;
        // id가 null이면 hashtag 필드를 기준으로 비교, 아니면 id 필드를 기준으로 비교
        return (id == null) ? Objects.equals(hashtag, that.hashtag) : Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        // id가 null이면 hashtag 필드로 해시 코드 생성, 아니면 id 필드로 해시 코드 생성
        return (id == null) ? Objects.hash(hashtag) : Objects.hash(id);
    }
}
