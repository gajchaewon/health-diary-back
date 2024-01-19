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
        if (o == null || getClass() != o.getClass()) return false;
        Hashtag hashtag = (Hashtag) o;
        return Objects.equals(id, hashtag.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
