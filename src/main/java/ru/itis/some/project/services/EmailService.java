package ru.itis.some.project.services;

import java.util.Map;

public interface EmailService {

    void sendEmail(String email, String template, Map<String, ?> modelMap);
}
