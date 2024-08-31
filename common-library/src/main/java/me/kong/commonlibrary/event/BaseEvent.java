package me.kong.commonlibrary.event;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor
@Getter
public class BaseEvent {

    private LocalDateTime createdAt;

    @Setter
    private LocalDateTime processedAt;

    private Type eventType;

    public BaseEvent(Type eventType) {
        this.createdAt = LocalDateTime.now();
        this.eventType = eventType;
    }

    public enum Type {
        GROUP_SIZE_INCREASE
    }
}