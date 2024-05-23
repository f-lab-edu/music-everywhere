package me.kong.groupservice.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.dto.request.GroupJoinRequestDto;
import me.kong.groupservice.dto.request.SaveGroupRequestDto;
import me.kong.groupservice.dto.response.GroupResponseDto;
import me.kong.groupservice.mapper.GroupMapper;
import me.kong.groupservice.service.GroupService;
import me.kong.groupservice.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;

    private final GroupMapper groupMapper;

    @PostMapping
    public ResponseEntity<GroupResponseDto> addGroup(@RequestBody @Valid SaveGroupRequestDto dto) {
        Group group = groupService.createNewGroup(dto);

        return ResponseEntity.ok(groupMapper.toDto(group));
    }

    @PostMapping("{groupId}")
    public ResponseEntity<HttpStatus> joinGroup(@PathVariable Long groupId, @RequestBody @Valid GroupJoinRequestDto dto) {
        groupService.joinGroup(dto, groupId);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
