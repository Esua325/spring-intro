package com.aptech.springintro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "students")
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 80, message = "Name must be 2 to 80 characters")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    private String email;

    @NotBlank(message = "Please select a course")
    private String course;

    @NotBlank(message = "Grade is required")
    @Pattern(regexp = "^[ABCDF]$", message = "Grade must be A, B, C, D, or F")
    private String grade;

    public Student() {}

    public Student(String name, String email, String course, String grade) {
        this.name = name;
        this.email = email;
        this.course = course;
        this.grade = grade;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    public String getGrade() { return grade; }
    public void setGrade(String grade) { this.grade = grade; }
}
