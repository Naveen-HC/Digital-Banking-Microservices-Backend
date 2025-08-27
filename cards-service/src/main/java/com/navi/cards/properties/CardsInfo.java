package com.navi.cards.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@ConfigurationProperties(prefix = "cards")
@Component
@Data
public class CardsInfo {
    private String message;
    private Map<String, String> contactDetails;
    private List<String> onCallSupport;
}
