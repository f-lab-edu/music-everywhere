package me.kong.groupservice.event;

import lombok.RequiredArgsConstructor;
import me.kong.commonlibrary.event.dto.GroupMemberIncreaseResponseDto;
import me.kong.groupservice.service.GroupService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static me.kong.commonlibrary.event.EventConstants.GROUP_MEMBER_INCREASE_RESPONSE;


@Service
@RequiredArgsConstructor
public class KafkaConsumer {

    private final GroupService groupService;

    @KafkaListener(
            topics = GROUP_MEMBER_INCREASE_RESPONSE,
            groupId = "group-increase-1",
            containerFactory = "groupIncreaseKafkaListenerContainerFactory")
    public void listen(GroupMemberIncreaseResponseDto dto) {
        groupService.increaseGroupSize(dto.getGroupId(), dto.getAdditionalMembers());
    }

}
