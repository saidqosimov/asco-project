package com.ascoproject.ascoproject.service;

import com.ascoproject.ascoproject.model.group.TelegramChat;
import com.ascoproject.ascoproject.model.group.TelegramChatResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TelegramService {

    @Value("${telegram.bot.token}") // application.properties dan
    private String botToken;

    private final RestTemplate restTemplate = new RestTemplate();

    public synchronized TelegramChat getChatInfo(Long chatId) {
        String url = "https://api.telegram.org/bot" + botToken + "/getChat?chat_id=" + chatId;
            ResponseEntity<TelegramChatResponse> response = restTemplate.getForEntity(url, TelegramChatResponse.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null && response.getBody().isOk()) {
                return response.getBody().getResult();
            }
        return null;
    }
}
