package com.example.mokeKafka2;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "load_test_input", groupId = "kafka-group")
    public void listen(String message) {
        try {
            // Преобразуем входящее сообщение в JsonNode для удобной работы с полями
            JsonNode jsonNode = objectMapper.readTree(message);

            // Получаем текущий user_id и создаём новый user_id
            String originalUserId = jsonNode.get("id").asText();
            String newUserId = "123";

            // Создаём новый объект JSON с заменённым user_id
            ((ObjectNode) jsonNode).put("id", newUserId);
            String updatedMessage = objectMapper.writeValueAsString(jsonNode);

            // Отправляем обновленное сообщение в выходной топик
            kafkaTemplate.send("mock_response_output", updatedMessage);

            // Логируем информацию о замене
            logger.info("Updated message: {} to: {}", objectMapper.readTree(message), updatedMessage);

        } catch (Exception e) {
            logger.error("Error processing message: {}", message, e);
        }
    }
}