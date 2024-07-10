package me.kong.groupservice.domain.entity.post;


import jakarta.persistence.*;
import lombok.*;
import me.kong.commonlibrary.entity.BaseTimeEntity;
import me.kong.groupservice.domain.entity.State;
import me.kong.groupservice.domain.entity.group.Group;
import me.kong.groupservice.domain.entity.profile.Profile;


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

    @ManyToOne(fetch = FetchType.LAZY)
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    private Profile profile;

    @Builder
    public Post(String title, String content, PostScope postScope, State state, Group group, Profile profile) {
        this.title = title;
        this.content = content;
        this.postScope = postScope;
        this.state = state;
        this.group = group;
        this.profile = profile;
    }
}
