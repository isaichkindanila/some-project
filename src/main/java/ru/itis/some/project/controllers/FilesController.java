package ru.itis.some.project.controllers;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import ru.itis.some.project.services.FileService;

import javax.servlet.http.HttpServletResponse;

@Controller
@AllArgsConstructor
@RequestMapping("/files")
public class FilesController {
    private final FileService fileService;

    @GetMapping
    public String getUploadPage() {
        return "files";
    }

    @PostMapping
    public String uploadFile(@RequestParam MultipartFile file) {
        if (file.getOriginalFilename() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "original file name must be provided");
        }

        fileService.save(file);
        return "files";
    }

    @SneakyThrows
    @GetMapping("/{fileName:.+}")
    public void getFile(@PathVariable String fileName, HttpServletResponse response) {
        var fileDto = fileService.load(fileName);

        response.setContentLengthLong(fileDto.getLength());
        response.setContentType(fileDto.getMimeType());
        response.setHeader("Content-Disposition", "inline; filename=\"" + fileDto.getOriginalName() + "\"");

        try(var in = fileDto.getUrl().openStream()) {
            var out = response.getOutputStream();

            in.transferTo(out);
            out.flush();
        }
    }
}
