package com.soup.merlinCastleBE.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum GameRoles {
    // Buoni
    MERLIN  (Faction.GOOD),
    PERCIVAL(Faction.GOOD),
    ARTHUR  (Faction.GOOD),
    LANCELOT(Faction.GOOD),
    SERVANT (Faction.GOOD),

    // Cattivi
    MORDRED (Faction.EVIL),
    MORGANA (Faction.EVIL),
    BRUTO   (Faction.EVIL),
    OBERON  (Faction.EVIL),
    LADY_ANNE(Faction.EVIL),
    MINION  (Faction.EVIL);

    private final Faction faction;

    public boolean isGood() {
        return this.faction == Faction.GOOD;
    }

    public boolean isEvil() {
        return this.faction == Faction.EVIL;
    }
}