package me.kong.paymentservice.domain.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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

    @Column(name = "user_id")
    private Long userId;

    @Builder
    public PayEvent(BigDecimal amount, Long userId) {
        this.amount = amount;
        this.userId = userId;
    }
}
