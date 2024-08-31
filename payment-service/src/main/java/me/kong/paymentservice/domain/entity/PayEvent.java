package me.kong.paymentservice.domain.entity;


import jakarta.persistence.*;
import lombok.*;
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

    @Setter
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Column(name = "user_id")
    private Long userId;

    @Embedded
    private BaseEvent baseEvent;

    @Builder
    public PayEvent(BigDecimal amount, Long userId, BaseEvent baseEvent) {
        this.amount = amount;
        this.userId = userId;
        this.baseEvent = baseEvent;
    }
}
