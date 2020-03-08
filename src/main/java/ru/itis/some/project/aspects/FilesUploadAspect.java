package ru.itis.some.project.aspects;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.itis.some.project.dto.FileDto;
import ru.itis.some.project.services.AuthService;
import ru.itis.some.project.services.EmailService;
import ru.itis.some.project.services.TemplateService;

import java.util.Map;

@Aspect
@Component
@RequiredArgsConstructor
public class FilesUploadAspect {

    private final AuthService authService;
    private final EmailService emailService;
    private final TemplateService templateService;

    @Value("${server.url}")
    private String serverURL;

    @AfterReturning(
            pointcut = "execution(* ru.itis.some.project.services.FileService.save(..)) && args(file,..)",
            returning = "dto",
            argNames = "file,dto"
    )
    public void sendUploadedNotificationEmail(MultipartFile file, FileDto dto) {
        @SuppressWarnings("ConstantConditions")
        var modelMap = Map.of(
                "server", serverURL,
                "fileName", file.getOriginalFilename(),
                "fileToken", dto.getToken()
        );

        var message = templateService.process("notify_upload", modelMap);
        var email = authService.getCurrentUser().getEmail();

        emailService.sendEmail(email, "File upload", message);
    }
}
