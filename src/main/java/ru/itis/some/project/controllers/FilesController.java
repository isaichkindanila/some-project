package ru.itis.some.project.controllers;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@AllArgsConstructor
@RequestMapping("/files")
public class FilesController {

    @GetMapping
    @ResponseBody
    public String getUploadPage() {
        return "it's working!";
    }
}
