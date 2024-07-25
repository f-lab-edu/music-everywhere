package me.kong.paymentservice.domain.repository;

import me.kong.paymentservice.domain.entity.PayEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PayEventRepository extends JpaRepository<PayEvent, Long> {
}
