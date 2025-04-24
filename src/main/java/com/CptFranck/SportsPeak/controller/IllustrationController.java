package com.CptFranck.SportsPeak.controller;

import com.CptFranck.SportsPeak.service.ExerciseService;
import com.CptFranck.SportsPeak.service.ExerciseTypeService;
import com.CptFranck.SportsPeak.service.MuscleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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
    private final String BASE_UPLOAD_DIR = "src/main/resources/static/uploads/";
    private final MuscleService muscleService;
    private final ExerciseService exerciseService;
    private final ExerciseTypeService exerciseTypeService;


    public IllustrationController(MuscleService muscleService, ExerciseTypeService exerciseTypeService, ExerciseService exerciseService) {
        this.muscleService = muscleService;
        this.exerciseTypeService = exerciseTypeService;
        this.exerciseService = exerciseService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{type}/{id}")
    public ResponseEntity<?> upload(@PathVariable String type, @PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {

        if (!ALLOWED_TYPES.contains(file.getContentType()))
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("Unauthorized file type.");
        if (MAX_FILE_SIZE < file.getSize())
            return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("File too large.");
        if (!ALLOWED_FOLDER_TYPE.contains(type))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Type not supported");

        String originalName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String filename = UUID.randomUUID() + "_" + originalName;

        String folder = BASE_UPLOAD_DIR + type + "s/";
        Files.createDirectories(Paths.get(folder));

        Path path = Paths.get(folder + filename);
        Files.write(path, file.getBytes());

        String publicPath = "/uploads/" + type + "s/" + filename;

        switch (type) {
            case "muscle":
            case "exercise":
            case "exerciseType":
        }

        return ResponseEntity.ok(Map.of("url", publicPath));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{type}/{filename}")
    public ResponseEntity<?> delete(@PathVariable String type, @PathVariable String filename) throws IOException {
        String path = BASE_UPLOAD_DIR + type + "s/" + filename;
        Files.deleteIfExists(Paths.get(path));
        return ResponseEntity.ok().build();
    }
}
