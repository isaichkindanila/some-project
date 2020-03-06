package ru.itis.some.project.services.impl;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itis.some.project.services.TemplateService;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

@Service
@AllArgsConstructor
public class TemplateServiceImpl implements TemplateService {
    private final Configuration config;

    @Override
    public String process(String templateName, Map<String, ?> modelMap) {
        try {
            var writer = new StringWriter();
            var template = config.getTemplate(templateName + ".ftl");

            template.process(modelMap, writer);

            return writer.toString();
        } catch (IOException | TemplateException e) {
            throw new IllegalStateException(e);
        }
    }
}
