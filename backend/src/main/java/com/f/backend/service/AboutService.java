package com.f.backend.service;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.f.backend.entity.About;
import com.f.backend.repository.AboutRepository;

import jakarta.transaction.Transactional;

@Service
public class AboutService {

    @Autowired
    private AboutRepository aboutRepository;

    @Value("${image.upload.dir}")
    private String uploadDir;

    public About getAboutUs() {
        return aboutRepository.findById(1L).orElse(null);
    }

    @Transactional
    public About saveOrUpdate(About about, MultipartFile cover) throws IOException {
        About oldAbout = aboutRepository.findById(1L).orElse(new About());

        if (about.getHeader() != null)
            oldAbout.setHeader(about.getHeader());
        if (about.getAddress() != null)
            oldAbout.setAddress(about.getAddress());
        if (about.getDescription() != null)
            oldAbout.setDescription(about.getDescription());
        
        if (cover != null && !cover.isEmpty()) {
            String coverName = saveImage(cover);
            oldAbout.setCover(coverName);
        }

        return aboutRepository.save(oldAbout);
    }

    private String saveImage(MultipartFile file) throws IOException {
        if (file.isEmpty())
            return null;

        Path uploadPath = Paths.get(uploadDir, "about");
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename != null ? originalFilename.substring(originalFilename.lastIndexOf("."))
                : "";
        String fileName = "1_" + UUID.randomUUID() + fileExtension;

        Path filePath = uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        return fileName;
    }
}
