package org.example.classgraphql.service;

import lombok.extern.slf4j.Slf4j;
import org.example.classgraphql.model.Course;
import org.example.classgraphql.model.CourseRating;
import org.example.classgraphql.model.CourseSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CourseService {

    private static final String BASE_URL = "http://course-service/courses";

    private final RestTemplate restTemplate;

    @Autowired
    public CourseService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Course createCourse(String title, String description, Long instructorId) {
        Course course = new Course();
        course.setTitle(title);
        course.setDescription(description);
        course.setInstructorId(instructorId);

        return restTemplate.postForObject(BASE_URL, course, Course.class);
    }

    public Course updateCourse(Long id, String title, String description) {
        Course course = new Course();
        course.setId(id);
        course.setTitle(title);
        course.setDescription(description);

        restTemplate.put(BASE_URL + "/" + id, course);
        return course;
    }

    @Cacheable(value = "course", key = "#courseId")
    public Optional<Course> findCourseById(Long courseId) {
        Course course = null;

        try {
            course = restTemplate.getForObject(BASE_URL + "/" + courseId, Course.class);
        } catch (Exception e) {
            log.error("An error occurred while fetching the course: {}", e.getMessage(), e);
        }
        return Optional.ofNullable(course);
    }

    public List<Course> findAllCourses() {
        Course[] courses = restTemplate.getForObject(BASE_URL, Course[].class);
        if (courses == null) {
            return Collections.emptyList();
        }

        return Arrays.asList(courses);
    }

    public CourseSession addSessionToCourse(Long courseId, String title) {
        CourseSession courseSession = new CourseSession();
        courseSession.setCourseId(courseId);
        courseSession.setTitle(title);

        CourseSession addedSession = restTemplate.postForObject(
                UriComponentsBuilder.fromUriString(BASE_URL + "/{courseId}/sessions")
                        .buildAndExpand(courseId).toUri(), courseSession, CourseSession.class);

        if (addedSession != null) {
            addedSession.setCourseId(courseId);
        }
        return addedSession;
    }

    public List<CourseSession> findAllSessionsByCourseId(Long courseId) {
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/{courseId}/sessions")
                .buildAndExpand(courseId).toUriString();

        CourseSession[] sessions = restTemplate.getForObject(url, CourseSession[].class);

        if (sessions == null) {
            return Collections.emptyList();
        }

        return Arrays.stream(sessions)
                .peek(session -> session.setCourseId(courseId))
                .collect(Collectors.toList());
    }

    public Optional<CourseSession> findSessionById(Long courseId, Long sessionId) {
        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/{courseId}/sessions/{sessionId}")
                .buildAndExpand(courseId, sessionId)
                .toUriString();

        try {
            CourseSession retrievedSession = restTemplate.getForObject(url, CourseSession.class);
            if (retrievedSession != null) {
                retrievedSession.setCourseId(courseId);
            }
            return Optional.ofNullable(retrievedSession);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public CourseRating addRatingToCourse(Long userId, Long courseId, Integer rating, String comment) {
        CourseRating courseRating = new CourseRating();
        courseRating.setUserId(userId);
        courseRating.setCourseId(courseId);
        courseRating.setRating(rating);
        courseRating.setComment(comment);

        String url = UriComponentsBuilder.fromUriString(BASE_URL + "/{courseId}/ratings")
                .buildAndExpand(courseId).toUriString();

        return restTemplate.postForObject(url, courseRating, CourseRating.class);
    }

    @Cacheable(value = "course", key = "#courseIds")
    public List<Course> findCourseByIds(List<Long> courseIds) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(BASE_URL);
        courseIds.forEach(id -> builder.queryParam("courseId", id));
        URI uri = builder.build().toUri();
        log.info("### findCourseByIds URI ###{}", uri.toString());
        Course[] courses = restTemplate.getForObject(uri, Course[].class);
        if (courses == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(courses);
    }
}
