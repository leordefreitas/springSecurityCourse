package com.leonardo.springSecurityCourse.controller;

import com.leonardo.springSecurityCourse.model.Student;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class StudentController {

    private List<Student> students = new ArrayList<>(List.of(
            new Student(1, "Leo", 60),
            new Student(2, "Mariana", 45)
    ));

    @GetMapping("/students")
    public List<Student> getStudents() {
        return students;
    }

    // faz-se isso para conseguir pegar o token e disponibilizar
    // quando for fazer o put delete e post, nao ha necessidade
    // de colocar isso no get por default
    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }

    @PostMapping("/students")
    public Student addStudent(@RequestBody Student student) {
        students.add(student);
        return student;
    }
}
