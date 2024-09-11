package com.example.courseservice;

import com.example.courseservice.domain.course.entity.Course;
import com.example.courseservice.domain.course.entity.CourseRating;
import com.example.courseservice.domain.course.repository.CourseRatingRepository;
import com.example.courseservice.domain.course.service.CourseRatingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseRatingServiceTest {

    @Mock
    private CourseRatingRepository courseRatingRepository;

    @InjectMocks
    private CourseRatingService courseRatingService;

    @Test
    public void testAddCourseRatingToCourse() {
        // given
        CourseRating courseRating = new CourseRating();
        Course course = new Course(1L);
        courseRating.setCourse(course);

        // when
        when(courseRatingRepository.save(any(CourseRating.class))).thenReturn(courseRating);

        // then
        CourseRating result = courseRatingService.addCourseRatingToCourse(1L, courseRating);
        assertNotNull(result);
        verify(courseRatingRepository).save(courseRating);
        assertEquals(1L, result.getCourse().getId());
    }

    @Test
    public void testUpdateCourseRating() {
        // given
        CourseRating existingRating = new CourseRating();
        existingRating.setId(1L);
        existingRating.setRating(5);
        existingRating.setComment("Great Course!");

        CourseRating updatedDetails = new CourseRating();
        updatedDetails.setRating(4);
        updatedDetails.setComment("Good Course...");

        // when
        when(courseRatingRepository.findById(1L)).thenReturn(Optional.of(existingRating));
        when(courseRatingRepository.save(any(CourseRating.class))).thenReturn(updatedDetails);
        CourseRating result = courseRatingService.updateCourseRating(1L, updatedDetails);

        // then
        assertNotNull(result);
        verify(courseRatingRepository).findById(1L);
        verify(courseRatingRepository).save(existingRating);
        assertEquals(4, result.getRating());
        assertEquals("Good Course...", result.getComment());
    }

    @Test
    public void testDeleteCourseRating() {
        // given & when
        doNothing().when(courseRatingRepository).deleteById(1L);
        courseRatingService.deleteRating(1L);

        // then
        verify(courseRatingRepository).deleteById(1L);
    }

    @Test
    public void testGetAllCourseRatingsByCourseId() {
        // given
        List<CourseRating> courseRatings = Arrays.asList(new CourseRating(), new CourseRating());

        // when
        when(courseRatingRepository.findByCourseId(1L)).thenReturn(courseRatings);
        List<CourseRating> result = courseRatingService.getAllRatingsByCourseId(1L);

        // then
        verify(courseRatingRepository).findByCourseId(1L);
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    public void testGetCourseRatingById() {
        // given
        CourseRating courseRating = new CourseRating();

        // when
        when(courseRatingRepository.findById(1L)).thenReturn(Optional.of(courseRating));
        Optional<CourseRating> result = courseRatingService.getCourseRatingById(1L);

        // then
        assertTrue(result.isPresent());
        assertSame(courseRating, result.get());
        verify(courseRatingRepository).findById(1L);
    }
}
