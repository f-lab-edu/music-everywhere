package me.kong.groupservice.domain.entity.profile;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kong.commonlibrary.entity.BaseTimeEntity;
import me.kong.groupservice.domain.entity.State;
import me.kong.groupservice.domain.entity.group.Group;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Profile extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    @Column(name = "group_role")
    @Enumerated(EnumType.STRING)
    private GroupRole groupRole;

    @Enumerated(EnumType.STRING)
    private State state;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Builder
    public Profile(String nickname, GroupRole groupRole, State state, Long userId, Group group) {
        this.nickname = nickname;
        this.groupRole = groupRole;
        this.state = state;
        this.userId = userId;
        this.group = group;
    }

    public void setState(State state) {
        this.state = state;
    }
}
