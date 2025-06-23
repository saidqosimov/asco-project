package com.ascoproject.ascoproject.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TelegramChat {
    private Long id;
    private String title;
    private String type;
    @JsonProperty("invite_link")
    private String inviteLink;
}
