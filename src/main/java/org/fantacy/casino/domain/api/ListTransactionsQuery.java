package org.fantacy.casino.domain.api;

public class ListTransactionsQuery {
    private String playerUid;

    public String getPlayerUid() {
        return playerUid;
    }

    public void setPlayerUid(String playerUid) {
        this.playerUid = playerUid;
    }
}
