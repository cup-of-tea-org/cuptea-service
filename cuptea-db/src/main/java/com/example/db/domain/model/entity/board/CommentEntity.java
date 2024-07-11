package com.example.db.domain.model.entity.board;

import com.example.db.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Getter
@EqualsAndHashCode(of = "commentId", callSuper = true)
@ToString(callSuper = true)
@Table(name = "comment")
public class CommentEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID commentId;

    @Column(name = "board_id")
    private UUID boardId;

    @Lob
    private String text;

    @Column(name = "use_yn", length = 1)
    private String useYn;

    @Column(name = "comment_parent_id")
    private UUID commentParentId;

    // 대댓글 깊이
    private Integer depth;

}
