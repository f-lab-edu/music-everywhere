package me.kong.paymentservice.domain.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kong.commonlibrary.event.BaseEvent;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "pay_event")
public class PayEvent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "user_id")
    private Long userId;

    @Embedded
    private BaseEvent baseEvent;

    @Builder
    public PayEvent(BigDecimal amount, PaymentStatus status, Long userId, BaseEvent baseEvent) {
        this.amount = amount;
        this.status = status;
        this.userId = userId;
        this.baseEvent = baseEvent;
    }
}
