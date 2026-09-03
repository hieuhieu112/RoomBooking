package com.app.backend.generator;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

public class EnumJsonGenerator {

    private static final String ENUM_PACKAGE =
            "com.app.backend.entity.enumm";

    private static final Path OUTPUT_DIRECTORY =
            Path.of("../shared/enums");

    public static void main(String[] args) throws Exception {

        Files.createDirectories(OUTPUT_DIRECTORY);

        ObjectMapper objectMapper = new ObjectMapper();

        for (Class<? extends Enum<?>> enumClass : findEnums()) {

            Map<String, Object> jsonData = new LinkedHashMap<>();

            for (Enum<?> enumValue : enumClass.getEnumConstants()) {

                Map<String, Object> metadata = new LinkedHashMap<>();

                /*
                 * Nếu enum có getUrl() thì lấy URL.
                 *
                 * Ví dụ:
                 *
                 * CREATE_BOOKING("/booking/{id}")
                 */
                try {
                    Method getUrl = enumClass.getMethod("getUrl");

                    if (getUrl.getReturnType() == String.class) {
                        Object url = getUrl.invoke(enumValue);

                        if (url != null) {
                            metadata.put("url", url);
                        }
                    }

                } catch (NoSuchMethodException ignored) {
                    // Enum không có getUrl() → chỉ lưu name
                }

                jsonData.put(enumValue.name(), metadata);
            }

            String fileName =
                    camelToKebab(enumClass.getSimpleName()) + ".json";

            Path outputPath =
                    OUTPUT_DIRECTORY.resolve(fileName);

            objectMapper
                    .writerWithDefaultPrettyPrinter()
                    .writeValue(outputPath.toFile(), jsonData);

            System.out.println(
                    "Generated: " + outputPath.toAbsolutePath()
            );
        }
    }

    /**
     * Find all enum classes inside ENUM_PACKAGE.
     */
    private static Class<? extends Enum<?>>[] findEnums()
            throws Exception {

        ClassLoader classLoader =
                Thread.currentThread().getContextClassLoader();

        String packagePath =
                ENUM_PACKAGE.replace('.', '/');

        Enumeration<URL> resources =
                classLoader.getResources(packagePath);

        java.util.List<Class<? extends Enum<?>>> enums =
                new java.util.ArrayList<>();

        while (resources.hasMoreElements()) {

            URL resource = resources.nextElement();

            if ("file".equals(resource.getProtocol())) {

                File directory =
                        new File(resource.toURI());

                File[] files =
                        directory.listFiles(
                                (dir, name) ->
                                        name.endsWith(".class")
                                                && !name.contains("$")
                        );

                if (files == null) {
                    continue;
                }

                for (File file : files) {

                    String className =
                            ENUM_PACKAGE
                                    + "."
                                    + file.getName()
                                    .replace(".class", "");

                    Class<?> clazz =
                            Class.forName(className);

                    if (clazz.isEnum()
                            && Enum.class.isAssignableFrom(clazz)
                            && !Modifier.isAbstract(clazz.getModifiers())) {

                        @SuppressWarnings("unchecked")
                        Class<? extends Enum<?>> enumClass =
                                (Class<? extends Enum<?>>) clazz;

                        enums.add(enumClass);
                    }
                }
            }
        }

        return enums.toArray(
                new Class[0]
        );
    }

    /**
     * DeviceStatus -> device-status
     * NotificationType -> notification-type
     */
    private static String camelToKebab(String value) {

        return value
                .replaceAll("([a-z])([A-Z])", "$1-$2")
                .toLowerCase();
    }
}