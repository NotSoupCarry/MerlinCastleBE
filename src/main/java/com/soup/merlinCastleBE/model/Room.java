package com.soup.merlinCastleBE.model;

import com.soup.merlinCastleBE.model.base.BaseModel;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room extends BaseModel {

    public enum Status { LOBBY, IN_PROGRESS, ENDED }

    @Column(nullable = false, unique = true, length = 10)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private Status status = Status.LOBBY;

    @Column(name = "player_count", nullable = false)
    @Builder.Default
    private int playerCount = 0;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "creator_id", nullable = false)
    private User creator;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "ended_on")
    private OffsetDateTime endedOn;

}
