package me.kong.groupservice.integration;


import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.domain.entity.group.GroupScope;
import me.kong.groupservice.domain.entity.group.JoinCondition;
import me.kong.groupservice.domain.repository.GroupJoinRequestRepository;
import me.kong.groupservice.domain.repository.GroupRepository;
import me.kong.groupservice.domain.repository.ProfileRepository;
import me.kong.groupservice.dto.request.GroupJoinRequestDto;
import me.kong.groupservice.dto.request.SaveGroupRequestDto;
import me.kong.groupservice.service.GroupJoinFacade;
import me.kong.groupservice.service.GroupService;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class GroupJoinServiceTest {

    @Autowired
    private GroupService groupService;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private GroupJoinFacade groupJoinFacade;

    @Autowired
    private GroupJoinRequestRepository joinRequestRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @BeforeEach
    public void setUp() {
        joinRequestRepository.deleteAll();
        profileRepository.deleteAll();
        groupRepository.deleteAll();
    }

    @Test
    public void testConcurrentGroupJoin() throws InterruptedException {
        SaveGroupRequestDto saveGroupRequestDto = SaveGroupRequestDto.builder()
                .groupName("test")
                .joinCondition(JoinCondition.OPEN)
                .groupScope(GroupScope.PUBLIC)
                .nickname("testuser")
                .description("desc")
                .build();
        Long userId = 1L;
        Long groupId = groupService.createNewGroup(saveGroupRequestDto, userId).getId();


        int numberOfThreads = 9;
        CountDownLatch latch = new CountDownLatch(numberOfThreads);
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        Group g = groupRepository.findAll().getFirst();

        for (long i = 2; i < numberOfThreads + 2; i++) {
            final Long uId = i;
            String nickname = "joinTest" + i;
            executorService.execute(() -> {
                try {

                    GroupJoinRequestDto dto = GroupJoinRequestDto.builder()
                            .nickname(nickname)
                            .requestInfo("info")
                            .build();
                    groupJoinFacade.joinGroup(dto, uId, g.getId());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Awaitility.await()
                .atMost(3, TimeUnit.SECONDS)
                .untilAsserted(() -> {
                    // 그룹에 성공적으로 가입된 인원의 수를 확인
                    Group group = groupRepository.findById(groupId).orElseThrow();
                    assertThat(group.getProfileCount()).isEqualTo(10);
                });
    }
}
