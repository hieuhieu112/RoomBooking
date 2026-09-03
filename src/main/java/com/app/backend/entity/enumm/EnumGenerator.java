package com.app.backend.entity.enumm;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class EnumGenerator {
    public static void main(String[] args) throws Exception {

        List<String> values = Arrays.stream(NotificationType.values())
                .map(Enum::name)
                .toList();

        Path output = Path.of("../shared/enums/notification-type.json");

        Files.createDirectories(output.getParent());

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper
                .writerWithDefaultPrettyPrinter()
                .writeValue(output.toFile(), values);

        System.out.println("Generated: " + output.toAbsolutePath());
    }
}
