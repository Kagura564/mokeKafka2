package com.example.mokeKafka2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "topic_TEST", groupId = "kafka-group")
    public void listen(String message) {

        System.out.println("Received message: " + message);

        try {
            // Преобразуем входящее сообщение в JsonNode для удобной работы с полями
            JsonNode jsonNode = objectMapper.readTree(message);

            // Получаем текущий user_id и создаём новый user_id
            String originalUserId = jsonNode.get("user_id").asText();
            String newUserId = "123";

            // Создаём новый объект JSON с заменённым user_id
            ((ObjectNode) jsonNode).put("user_id", newUserId);
            String updatedMessage = objectMapper.writeValueAsString(jsonNode);

            // Отправляем обновленное сообщение в выходной топик
            kafkaTemplate.send("mock_response_output", updatedMessage);

            // Выводим информацию о замене в консоль
            System.out.println("Заменил сообщение: " + message + " на: " + updatedMessage);

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Ошибка обработки сообщения: " + message);
        }
    }
}