package me.kong.groupservice.domain.entity.GroupJoinRequest;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.kong.commonlibrary.entity.BaseTimeEntity;
import me.kong.groupservice.domain.entity.group.Group;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class GroupJoinRequest extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requestInfo;

    private String nickname;

    @Column(name = "response")
    @Enumerated(EnumType.STRING)
    private JoinResponse response;

    @Column(name = "user_id")
    private Long userId;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Builder
    public GroupJoinRequest(Long id, String requestInfo, String nickname, JoinResponse response, Long userId, Group group) {
        this.id = id;
        this.requestInfo = requestInfo;
        this.nickname = nickname;
        this.response = response;
        this.userId = userId;
        this.group = group;
    }

    public void approveJoinRequest() {
        response = JoinResponse.APPROVED;
    }

    public void rejectJoinRequest() {
        response = JoinResponse.REJECTED;
    }
}
