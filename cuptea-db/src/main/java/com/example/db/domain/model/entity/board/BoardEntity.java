package com.example.db.domain.model.entity.board;

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
@EqualsAndHashCode(of = "boardId", callSuper = true)
@ToString(callSuper = true)
@Table(name = "board")
/**
 * 방명록
 */
public class BoardEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID boardId;

    @Column(name = "member_id")
    private UUID memberId;

    @Lob
    private String text;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(name = "like_count")
    private Integer likeCount;

    @Column(name = "board_count")
    private Integer boardCount;

    @Column(name = "use_yn", length = 1)
    private String useYn;

    @PrePersist
    public void prePersist() {
        useYn = ObjectUtils.defaultIfNull(useYn, "Y");
    }
}
