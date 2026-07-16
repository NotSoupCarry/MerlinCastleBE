package com.soup.merlinCastleBE.model;

import com.soup.merlinCastleBE.enums.Faction;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "game_sessions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GameSession {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Enumerated(EnumType.STRING)
    @Column(name = "winner_faction", length = 10)
    private Faction winnerFaction;

    @Column(name = "total_rounds", nullable = false)
    @Builder.Default
    private int totalRounds = 0;

    @Column(name = "started_at", nullable = false, updatable = false)
    private OffsetDateTime startedAt;

    @Column(name = "ended_at")
    private OffsetDateTime endedAt;

    @OneToMany(mappedBy = "gameSession", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<GamePlayer> players = new ArrayList<>();

    @OneToMany(mappedBy = "gameSession", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("questNumber ASC")
    @Builder.Default
    private List<QuestResult> questResults = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.startedAt = OffsetDateTime.now();
    }
}
