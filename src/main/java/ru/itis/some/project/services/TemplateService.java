package ru.itis.some.project.services;

import java.util.Map;

public interface TemplateService {
    String process(String templateName, Map<String, ?> modelMap);
}
