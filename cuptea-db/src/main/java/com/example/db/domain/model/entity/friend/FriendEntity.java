package com.example.db.domain.model.entity.friend;

import com.example.db.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.ObjectUtils;

import java.util.UUID;

@Entity
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@EqualsAndHashCode(of = "id", callSuper = true)
@ToString(callSuper = true)
@Table(name = "friend")
public class FriendEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "member_id")
    private UUID memberId;

    @Column(name = "is_friend", length = 1)
    private String isFriend;

    @Column(name = "block_count")
    private Integer blockCount;

    @Column(name = "friend_login_id")
    private String friendLoginId;

    @PrePersist
    public void prePersist() {
        isFriend = ObjectUtils.defaultIfNull(isFriend, "N");
        blockCount = ObjectUtils.defaultIfNull(blockCount, 0);
    }

}
