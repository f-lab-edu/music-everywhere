package me.kong.groupservice.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kong.commonlibrary.event.dto.GroupMemberIncreaseRequestDto;
import me.kong.commonlibrary.util.JwtReader;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.dto.request.GroupJoinProcessDto;
import me.kong.groupservice.dto.request.GroupJoinRequestDto;
import me.kong.groupservice.dto.request.enums.JoinRequestSearchCondition;
import me.kong.groupservice.dto.request.SaveGroupRequestDto;
import me.kong.groupservice.dto.response.GroupJoinResponseDto;
import me.kong.groupservice.dto.response.GroupResponseDto;
import me.kong.groupservice.mapper.GroupJoinRequestMapper;
import me.kong.groupservice.mapper.GroupMapper;
import me.kong.groupservice.mapper.GroupMemberIncreaseRequestMapper;
import me.kong.groupservice.service.GroupJoinFacade;
import me.kong.groupservice.service.GroupJoinRequestService;
import me.kong.groupservice.service.GroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static me.kong.commonlibrary.constant.HttpStatusResponseEntity.RESPONSE_OK;

@Slf4j
@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final GroupJoinRequestService joinRequestService;
    private final GroupMapper groupMapper;
    private final GroupJoinRequestMapper requestMapper;
    private final GroupMemberIncreaseRequestMapper memberIncreaseRequestMapper;
    private final GroupJoinFacade groupJoinFacade;
    private final JwtReader jwtReader;

    @PostMapping
    public ResponseEntity<GroupResponseDto> addGroup(@RequestBody @Valid SaveGroupRequestDto dto) {
        Group group = groupService.createNewGroup(dto, jwtReader.getUserId());

        return ResponseEntity.ok(groupMapper.toDto(group));
    }

    @PostMapping("{groupId}")
    public ResponseEntity<HttpStatus> joinGroup(@PathVariable Long groupId, @RequestBody @Valid GroupJoinRequestDto dto) {
        groupJoinFacade.joinGroup(dto, jwtReader.getUserId(), groupId);

        return RESPONSE_OK;
    }

    @PostMapping("{groupId}/increase-group-size")
    public ResponseEntity<HttpStatus> increaseGroupSize(
            @PathVariable Long groupId,
            @RequestParam(required = true) Integer size) {
        GroupMemberIncreaseRequestDto dto = memberIncreaseRequestMapper.toDto(groupId, jwtReader.getUserId(), size);

        groupService.requestIncreaseGroupSize(dto);

        return RESPONSE_OK;
    }

    @GetMapping("{groupId}/requests")
    public ResponseEntity<List<GroupJoinResponseDto>> getGroupRequests(
            @PathVariable Long groupId,
            @RequestParam(name = "status", defaultValue = "PENDING") JoinRequestSearchCondition condition) {
        List<GroupJoinResponseDto> requests =
                requestMapper.toDtoList(joinRequestService
                        .getGroupJoinRequestsByGroupIdAndCondition(jwtReader.getUserId(), groupId, condition));

        return new ResponseEntity<>(requests, HttpStatus.OK);
    }

    @PatchMapping("{groupId}/requests/{requestId}")
    public ResponseEntity<HttpStatus> handleGroupJoinRequest(@PathVariable Long groupId,
                                                             @PathVariable Long requestId,
                                                             @RequestBody GroupJoinProcessDto processDto) {
        groupJoinFacade.processGroupJoinRequest(jwtReader.getUserId(), groupId, requestId, processDto);

        return RESPONSE_OK;
    }
}
