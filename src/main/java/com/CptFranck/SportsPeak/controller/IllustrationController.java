package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.domain.entity.MuscleEntity;
import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
import com.CptFranck.SportsPeak.service.MuscleService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/api/illustrations")
public class IllustrationController {

    private static final long MAX_FILE_SIZE = 1024 * 1024; // 1MB
    private static final List<String> ALLOWED_TYPES = List.of("image/png");
    private static final List<String> ALLOWED_FOLDER_TYPE = List.of("muscle", "exercise", "exerciseType");
    private final String BASE_UPLOAD_DIR = "static/uploads/";
    private final MuscleService muscleService;
    private final ExerciseService exerciseService;
    private final ExerciseTypeService exerciseTypeService;


    public IllustrationController(MuscleService muscleService, ExerciseTypeService exerciseTypeService, ExerciseService exerciseService) {
        this.muscleService = muscleService;
        this.exerciseTypeService = exerciseTypeService;
        this.exerciseService = exerciseService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/upload/{type}/{id}")
    public ResponseEntity<?> uploadImage(@PathVariable String type, @PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            if (!ALLOWED_TYPES.contains(file.getContentType()))
                return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Unauthorized file type.");
            if (MAX_FILE_SIZE < file.getSize())
                return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File too large.");
            if (!ALLOWED_FOLDER_TYPE.contains(type))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Type not supported");

            String safeName = Objects.requireNonNull(file.getOriginalFilename()).replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
            String filename = UUID.randomUUID() + "_" + safeName;
            Path filePath = Paths.get(BASE_UPLOAD_DIR + type + "s/" + filename);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, file.getBytes());

            String imageUrl = "/api/illustrations/" + type + "s/" + filename;

            switch (type) {
                case "muscle":
                    this.updateMuscleIllustrationPath(id, imageUrl);
                    break;
                case "exercise":
                    //                this.updateExerciseIllustrationPath(id, imageUrl);
                    break;
                case "exerciseType":
                    //                this.updateExerciseTypeIllustrationPath(id, imageUrl);
                    break;
                default:
                    return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body("Type not supported");
            }

            return ResponseEntity.ok(Map.of("url", imageUrl));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Upload failed");
        }
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get(BASE_UPLOAD_DIR).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_PNG)
                        .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    private void updateMuscleIllustrationPath(Long muscleId, String illustrationPath) {
        MuscleEntity muscle = muscleService.findOne(muscleId);
        muscle.setIllustrationPath(illustrationPath);
        muscleService.save(muscle);
    }

//    private void updateExerciseIllustrationPath(Long exerciseId, String illustrationPath) {
//        ExerciseEntity exerciseEntity = exerciseService.findOne(exerciseId).orElseThrow(() -> new ExerciseTypeNotFoundException(muscleId));
//        exerciseEntity.setIllustrationPath(illustrationPath);
//        exerciseService.save(exerciseEntity);
//    }
//
//    private void updateExerciseTypeIllustrationPath(Long exerciseTypeId, String illustrationPath) {
//        ExerciseTypeEntity exerciseType = exerciseTypeService.findOne(exerciseTypeId).orElseThrow(() -> new ExerciseTypeNotFoundException(muscleId));
//        exerciseType.setIllustrationPath(illustrationPath);
//        exerciseTypeService.save(muscle);
//    }
}
