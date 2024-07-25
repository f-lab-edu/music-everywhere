package me.kong.paymentservice.domain.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "pay_event")
public class PayEvent {

    @Id
    private Long id;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "user_id")
    private Long userId;

    @Builder
    public PayEvent(BigDecimal amount, State state, Long userId) {
        this.amount = amount;
        this.state = state;
        this.userId = userId;
    }
}
