package com.soup.merlinCastleBE.model;

import com.soup.merlinCastleBE.enums.Faction;
import com.soup.merlinCastleBE.enums.GameRoles;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "game_players")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_session_id", nullable = false)
    private GameSession gameSession;

    // NULL se ospite non registrato
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "user_id", nullable = true)
    private User user;

    @Column(name = "username_snapshot", nullable = false, length = 50)
    private String usernameSnapshot;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private GameRoles role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Faction faction;

    @Column(name = "joined_at", nullable = false, updatable = false)
    private OffsetDateTime joinedAt;

    @PrePersist
    protected void onCreate() {
        this.joinedAt = OffsetDateTime.now();
    }

    public boolean isGuest() {
        return this.user == null;
    }
}