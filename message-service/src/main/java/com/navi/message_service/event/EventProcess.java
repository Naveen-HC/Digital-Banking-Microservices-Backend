package com.navi.message_service.event;

import com.navi.message_service.dto.EventDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
@Slf4j
public class EventProcess {

    @Bean
    public Function<EventDto, EventDto> sms() {
        return event -> {
            log.info("sending {} sms to {}'s mobile-number: {}", event.getMessage(), event.getName(), event.getMobileNumber());
            return event;
        };
    }

    @Bean
    public Function<EventDto, EventDto> email() {
        return event -> {
            log.info("Sending {} email to {}'s email-address: {}", event.getMessage(), event.getName(), event.getMobileNumber());
            return event;
        };
    }
}
