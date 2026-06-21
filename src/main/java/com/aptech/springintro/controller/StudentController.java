package com.aptech.springintro.controller;

import com.aptech.springintro.client.CourseClient;
import com.aptech.springintro.model.CourseDTO;
import com.aptech.springintro.model.Student;
import com.aptech.springintro.repository.StudentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Controller
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseClient courseClient;

    // ── HOME ────────────────────────────────────────────────
    @GetMapping("/")
    public String home() {
        return "redirect:/students";
    }

    // ── LIST ALL STUDENTS ────────────────────────────────────
    @GetMapping("/students")
    public String listStudents(Model model) {
        model.addAttribute("students", studentRepository.findAll());

        // Fetch courses from course-service via OpenFeign
        // If course-service is down, show empty list gracefully
        List<CourseDTO> courses;
        try {
            courses = courseClient.getAllCourses();
        } catch (Exception e) {
            System.err.println("⚠️ Could not reach course-service: " + e.getMessage());
            courses = Collections.emptyList();
        }
        model.addAttribute("courses", courses);

        return "students";
    }

    // ── SHOW ADD FORM ────────────────────────────────────────
    @GetMapping("/students/add")
    public String showAddForm(Model model) {
        model.addAttribute("student", new Student());

        List<CourseDTO> courses;
        try {
            courses = courseClient.getAllCourses();
        } catch (Exception e) {
            courses = Collections.emptyList();
        }
        model.addAttribute("courses", courses);

        return "add-student";
    }

    // ── PROCESS ADD FORM ─────────────────────────────────────
    @PostMapping("/students/add")
    public String addStudent(@Valid @ModelAttribute("student") Student student,
                             BindingResult result, Model model) {
        if (result.hasErrors()) {
            try {
                model.addAttribute("courses", courseClient.getAllCourses());
            } catch (Exception e) {
                model.addAttribute("courses", Collections.emptyList());
            }
            return "add-student";
        }
        studentRepository.save(student);
        return "redirect:/students";
    }

    // ── SHOW EDIT FORM ───────────────────────────────────────
    @GetMapping("/students/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student not found: " + id));
        model.addAttribute("student", student);

        List<CourseDTO> courses;
        try {
            courses = courseClient.getAllCourses();
        } catch (Exception e) {
            courses = Collections.emptyList();
        }
        model.addAttribute("courses", courses);

        return "edit-student";
    }

    // ── PROCESS EDIT FORM ────────────────────────────────────
    @PostMapping("/students/edit/{id}")
    public String editStudent(@PathVariable int id,
                              @Valid @ModelAttribute("student") Student student,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "edit-student";
        }
        student.setId(id);
        studentRepository.save(student);
        return "redirect:/students";
    }

    // ── DELETE STUDENT ───────────────────────────────────────
    @GetMapping("/students/delete/{id}")
    public String deleteStudent(@PathVariable int id) {
        studentRepository.deleteById(id);
        return "redirect:/students";
    }
}
