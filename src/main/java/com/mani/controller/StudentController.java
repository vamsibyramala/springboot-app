package com.mani.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.mani.entity.Student;
import com.mani.repo.StudentRepository;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    // Show all students (JSON)
    @GetMapping("/showAll")
    public ResponseEntity<List<Student>> getAllStudents() {
        return ResponseEntity.ok(studentRepository.findAll());
    }

    // Register new student
    @PostMapping("/register")
    public ResponseEntity<String> registerStudent(@RequestBody Student student) {
        try {
            if (studentRepository.findByUsername(student.getUsername()) != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("⚠️ Username already exists!");
            }
            studentRepository.save(student);
            return ResponseEntity.status(HttpStatus.CREATED).body("✅ Student registered successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("❌ Error: " + e.getMessage());
        }
    }

    // Login
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Student student) {
        Student existing = studentRepository.findByUsername(student.getUsername());

        if (existing == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Invalid username");
        }
        if (!existing.getPassword().equals(student.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("❌ Invalid password");
        }

        return ResponseEntity.ok("✅ Login successful! Welcome " + existing.getUsername());
    }

    // Serve static pages
    @GetMapping("/page/login")
    public ResponseEntity<?> loginPage() {
        return ResponseEntity.ok(new ClassPathResource("static/login.html"));
    }

    @GetMapping("/page/register")
    public ResponseEntity<?> registerPage() {
        return ResponseEntity.ok(new ClassPathResource("static/register.html"));
    }

    @GetMapping("/page/dashboard")
    public ResponseEntity<?> dashboardPage() {
        return ResponseEntity.ok(new ClassPathResource("static/index.html"));
    }
}
