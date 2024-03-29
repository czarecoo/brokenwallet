package org.fantacy.casino.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.FetchType.EAGER;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = EAGER)
    @JoinColumn(name = "account_id")
    private Account account;
    private String direction;
    @Column(name = "external_uid")
    private String externalUid;
    private Double amount;
    private Double balanceBefore;
    private Double balanceAfter;

    public Transaction(Account account, String direction, String externalUid, Double amount, Double balanceBefore, Double balanceAfter) {
        this.account = account;
        this.direction = direction;
        this.externalUid = externalUid;
        this.amount = amount;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
    }
}
