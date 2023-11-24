package kr.bb.store.domain.question.entity;

import kr.bb.store.domain.common.entity.BaseEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Answer extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @MapsId
    @OneToOne
    @JoinColumn(name="id")
    private Question question;

    @NotNull
    private String content;

    @Builder
    public Answer(Question question, String content) {
        this.question = question;
        this.content = content;
    }
}
