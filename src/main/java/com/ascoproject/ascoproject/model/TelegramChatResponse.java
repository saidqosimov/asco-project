package com.ascoproject.ascoproject.model;

import lombok.Data;

@Data
public class TelegramChatResponse {
    private boolean ok;
    private TelegramChat result;
}
