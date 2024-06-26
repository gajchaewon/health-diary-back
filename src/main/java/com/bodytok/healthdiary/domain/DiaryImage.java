package com.bodytok.healthdiary.domain;


import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Getter
@Entity
public class DiaryImage extends AuditingFields implements IBaseImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @Setter
    @Column(nullable = false)
    private String originalFileName;

    @Setter
    @Column(nullable = false, unique = true)
    private String savedFileName;

    @Setter
    @Column(nullable = false)
    private String imageUrl;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diary_id")
    private PersonalExerciseDiary personalExerciseDiary;


    public DiaryImage(){}

    @Builder
    public DiaryImage(String originalFileName, String savedFileName, String imageUrl){
        this.originalFileName = originalFileName;
        this.savedFileName = savedFileName;
        this.imageUrl = imageUrl;
    }

}
