package com.aptech.springintro.client;

import com.aptech.springintro.model.CourseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "course-service", url = "${course.service.url:http://localhost:8082}")
public interface CourseClient {

    @GetMapping("/api/courses")
    List<CourseDTO> getAllCourses();
}
