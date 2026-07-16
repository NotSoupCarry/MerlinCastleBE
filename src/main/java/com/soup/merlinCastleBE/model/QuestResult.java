package com.soup.merlinCastleBE.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "quest_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestResult {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_session_id", nullable = false)
    private GameSession gameSession;

    @Column(name = "quest_number", nullable = false)
    private int questNumber;

    @Column(name = "team_size", nullable = false)
    private int teamSize;

    @Column(nullable = false)
    private boolean success;

    @Column(name = "fail_votes", nullable = false)
    @Builder.Default
    private int failVotes = 0;

    @Column(name = "completed_at", nullable = false, updatable = false)
    private OffsetDateTime completedAt;

    @PrePersist
    protected void onCreate() {
        this.completedAt = OffsetDateTime.now();
    }
}