package com.littlebank.finance.global.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class FileUtil {

    public static String readFileFromResource(String path) {
        try {
            ClassPathResource resource = new ClassPathResource(path);
            byte[] bytes = resource.getInputStream().readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("파일을 읽을 수 없습니다: " + path, e);
        }
    }
}
