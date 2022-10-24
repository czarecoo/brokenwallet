package org.fantacy.casino.domain.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "player_uid")
    private String playerUid;

    public Account() {
    }

    public Account(String playerUid) {
        this.playerUid = playerUid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayerUid() {
        return playerUid;
    }

    public void setPlayerUid(String playerUid) {
        this.playerUid = playerUid;
    }
}
