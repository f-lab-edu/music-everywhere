package me.kong.groupservice.domain.entity.group;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kong.commonlibrary.entity.BaseTimeEntity;
import me.kong.groupservice.domain.entity.profile.Profile;
import me.kong.groupservice.domain.entity.State;

import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "music_group")
public class Group extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Column(name = "group_size")
    private Integer groupSize;

    @Column(name = "join_condition")
    @Enumerated(EnumType.STRING)
    private JoinCondition joinCondition;

    @Column(name = "group_scope")
    @Enumerated(EnumType.STRING)
    private GroupScope groupScope;

    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "owner_user_id")
    private Long ownerUserId;

    @Builder
    public Group(String name, String description, Integer groupSize, JoinCondition joinCondition, GroupScope groupScope, State state, Long ownerUserId) {
        this.name = name;
        this.description = description;
        this.groupSize = groupSize;
        this.joinCondition = joinCondition;
        this.groupScope = groupScope;
        this.state = state;
        this.ownerUserId = ownerUserId;
    }

}
