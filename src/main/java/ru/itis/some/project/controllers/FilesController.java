package ru.itis.some.project.controllers;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import ru.itis.some.project.services.FileService;

@Controller
@AllArgsConstructor
@RequestMapping("/files")
public class FilesController {

    private final FileService fileService;

    @GetMapping
    public ModelAndView getUploadPage() {
        return new ModelAndView("files", "files", fileService.findByCurrentUser());
    }

    @PostMapping
    public ModelAndView uploadFile(@RequestParam MultipartFile file) {
        if (file.getOriginalFilename() == null || file.getOriginalFilename().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "original file name must be provided");
        }

        fileService.save(file);
        return getUploadPage();
    }

    @GetMapping("/{fileName:.+}")
    @SneakyThrows
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {
        var fileDto = fileService.load(fileName);

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileDto.getOriginalName() +  "\"");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(fileDto.getLength())
                .contentType(MediaType.parseMediaType(fileDto.getMimeType()))
                .body(fileDto.getResource());
    }
}
