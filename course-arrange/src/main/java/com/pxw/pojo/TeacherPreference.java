package com.pxw.pojo;

import lombok.Data;

/**
 * Teacher time preference entity class
 */
@Data
public class TeacherPreference {
    private Integer id;
    private Integer teacherId;
    private Integer timeslotId;
    private Integer preferenceScore;  // Preference score: 1-10, 10 means like very much, 1 means dislike

    public TeacherPreference() {}

    public TeacherPreference(Integer teacherId, Integer timeslotId, Integer preferenceScore) {
        this.teacherId = teacherId;
        this.timeslotId = timeslotId;
        this.preferenceScore = preferenceScore;
    }
}
