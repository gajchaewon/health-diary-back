package com.bodytok.healthdiary.domain;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Entity
public class DiaryImage extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Column(nullable = false)
    private String originalFileName;

    @Column(nullable = false, unique = true)
    private String savedFileName;

    @Column(nullable = false)
    private String filePath;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private PersonalExerciseDiary personalExerciseDiary;


    protected DiaryImage(){}

    private DiaryImage(String originalFileName, String savedFileName, String filePath){
        this.originalFileName = originalFileName;
        this.savedFileName = savedFileName;
        this.filePath = filePath;
    }

    public static DiaryImage of(String originalFileName, String savedFileName, String filePath){
        return new DiaryImage(originalFileName, savedFileName, filePath);
    }

}
