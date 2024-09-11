package com.example.courseservice.domain.course.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@Table(name = "courses")
@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column
    private String description;

    @Column(name = "instructor_id", nullable = false)
    private Long instructorId;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @OneToMany(mappedBy = "course")
    @JsonManagedReference // 순환참조 방지 (연관관계의 주인에는 @JsonBackReference 선언)
    private List<CourseSession> courseSessions;

    @OneToMany(mappedBy = "course")
    @JsonManagedReference // 순환참조 방지 (연관관계의 주인에는 @JsonBackReference 선언)
    private List<CourseRating> courseRatings;

    public Course(Long id) {
        this.id = id;
    }
}
