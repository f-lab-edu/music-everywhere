package me.kong.paymentservice.event.consumer;

import me.kong.paymentservice.dto.event.GroupMemberIncreaseRequestDto;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;



@Service
public class GroupIncreaseConsumer {

    @KafkaListener(
            topics = "dev.group.payment.request.increase-group-size",
            groupId = "group-increase-1",
            containerFactory = "groupIncreaseKafkaListenerContainerFactory")
    public void listen(GroupMemberIncreaseRequestDto message) {

        System.out.println("Received message: " + message);
    }
}
