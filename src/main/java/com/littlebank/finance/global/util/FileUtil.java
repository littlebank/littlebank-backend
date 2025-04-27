package com.littlebank.finance.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtil {

    public static String readFileFromResource(String path) {
        try {
            ClassPathResource resource = new ClassPathResource(path);
            byte[] bytes = Files.readAllBytes(resource.getFile().toPath());
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("파일을 읽을 수 없습니다: " + path, e);
        }
    }
}
