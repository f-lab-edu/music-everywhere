package me.kong.groupservice.domain.entity.post;


import jakarta.persistence.*;
import lombok.*;
import me.kong.commonlibrary.entity.BaseTimeEntity;
import me.kong.groupservice.domain.entity.State;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity(name = "posts")
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String content;

    @Column(name = "scope")
    @Enumerated(EnumType.STRING)
    private PostScope postScope;

    @Setter
    @Enumerated(EnumType.STRING)
    private State state;

    private Long groupId;

    private Long profileId;

    @Builder
    public Post(String title, String content, PostScope postScope, State state, Long groupId, Long profileId) {
        this.title = title;
        this.content = content;
        this.postScope = postScope;
        this.state = state;
        this.groupId = groupId;
        this.profileId = profileId;
    }
}
