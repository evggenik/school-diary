package com.evggenn.school.person;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@AllArgsConstructor
@Data
public class PersonService {

    private final PersonRepo personRepo;
    private final String uploadDir = "uploads/";

    public String uploadAvatar(Long personId, MultipartFile file) throws IOException {

        Person person = personRepo.findById(personId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        validateFile(file);

        String filePath = saveFile(file, personId);

        person.setAvatarUrl(filePath);
        personRepo.save(person);

        return filePath;
    }
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("File not selected");
        }

        String contentType = file.getContentType();
        if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
            throw new RuntimeException("File format is not available.JPEG and PNG only.");
        }

        long fileSize = file.getSize();
        if (fileSize > 1024 * 1024) {
            throw new RuntimeException("File size is more than 1MB.");
        }
    }
    private String saveFile(MultipartFile file, Long personId) throws IOException {

        String userDir = uploadDir + "person_" + personId + "/";
        File directory = new File(userDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        String filePath = userDir + file.getOriginalFilename();

        Path path = Paths.get(filePath);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return filePath;
    }

}
