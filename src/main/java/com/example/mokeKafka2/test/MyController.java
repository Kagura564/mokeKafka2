package com.example.mokeKafka2.test;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Random;

@RestController
@RequestMapping("/test")
public class MyController {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Random random = new Random();

    @PostMapping("/my")
    public ResponseEntity<String> handlePostRequest(@RequestBody String jsonPayload) {
        try {
            // Преобразуем входящий JSON в JsonNode для удобной работы с полями
            JsonNode jsonNode = objectMapper.readTree(jsonPayload);

            // Генерируем случайное значение для "id"
            String newId = String.valueOf(random.nextInt(10000)); // Генерируем случайное число от 0 до 9999

            // Обновляем поле "id" в JSON
            ((ObjectNode) jsonNode).put("id", newId);

            // Преобразуем измененный JsonNode обратно в строку JSON
            String updatedJson = objectMapper.writeValueAsString(jsonNode);

            // Возвращаем измененный JSON в ответе
            return ResponseEntity.ok(updatedJson);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Ошибка обработки JSON");
        }
    }
}