package com.f.backend.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.f.backend.entity.About;
import com.f.backend.service.AboutService;

@RestController
@RequestMapping("/api/about")
@CrossOrigin(origins = "*")  // Allow Angular to access API
public class AboutController {

    private final AboutService aboutService;

    public AboutController(AboutService aboutService) {
        this.aboutService = aboutService;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAbout() {
        About about = aboutService.getAboutUs();
        if (about == null) {
            return ResponseEntity.status(404).body(Map.of("message", "No About Us data found"));
        }
        return ResponseEntity.ok(Map.of("about", about));
    }

    @PutMapping
    public ResponseEntity<Map<String, String>> updateAbout(
            @RequestPart("about") About about,
            @RequestPart(value = "cover", required = false) MultipartFile cover,
            @RequestPart(value = "officemage", required = false) MultipartFile officemage) {
        try {
            aboutService.saveOrUpdate(about, cover, officemage);
            return ResponseEntity.ok(Map.of("message", "Updated Successfully!"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("error", "Failed to update About Us"));
        }
    }
}
