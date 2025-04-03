<<<<<<< Updated upstream:src/main/java/com/lhj/fitnessbooking/domain/course/dto/CourseInfoTmp.java
package com.lhj.fitnessbooking.domain.course.dto;
=======
package com.lhj.fitnessbooking.dto;
>>>>>>> Stashed changes:src/main/java/com/lhj/fitnessbooking/dto/CourseInfoTmp.java

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class CourseInfoTmp {

    private Long courseId;
    private String instructorName;
    private String instructorImgUrl;
    private String courseName;
    private LocalTime courseStartTime;
    private LocalTime courseEndTime;
    private int attendeeCount;
}
