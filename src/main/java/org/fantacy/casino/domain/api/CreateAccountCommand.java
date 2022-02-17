package org.fantacy.casino.domain.api;

public class CreateAccountCommand {
    public String getPlayerUid() {
        return playerUid;
    }

    public void setPlayerUid(String playerUid) {
        this.playerUid = playerUid;
    }

    private String playerUid;
}
