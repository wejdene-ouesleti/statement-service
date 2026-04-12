package com.example.statement_service.service;

import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class TemplateService {

    public String generateHtml(Map<String, Object> data) throws Exception {
        Handlebars handlebars = new Handlebars();

        String templateContent = new String(
                getClass().getResourceAsStream("/templates/statement-template.hbs").readAllBytes(),
                StandardCharsets.UTF_8
        );

        Template template = handlebars.compileInline(templateContent);

        return template.apply(data);
    }
}