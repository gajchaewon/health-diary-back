package com.bodytok.healthdiary.domain;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;


@Getter
public class Exercise {

    private final String id;

    @Setter
    private String exerciseName;

    @Setter
    private String description;

    @JsonCreator
    private Exercise(
            @JsonProperty("exerciseName") String exerciseName,
            @JsonProperty("description") String description) {
        this.id = UUID.randomUUID().toString();
        this.exerciseName = exerciseName;
        this.description = description;
    }

    public static Exercise of(String exerciseName, String description) {
        return new Exercise(exerciseName, description);
    }

    public void changeInfo(String exerciseName, String description) {
        this.exerciseName = exerciseName;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Exercise exercise = (Exercise) o;
        return Objects.equals(id, exercise.id) && Objects.equals(exerciseName, exercise.exerciseName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, exerciseName);
    }
}
