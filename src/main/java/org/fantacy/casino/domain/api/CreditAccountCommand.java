package org.fantacy.casino.domain.api;

public class CreditAccountCommand {
    private Long account;
    private Double amount;
    private String externalUid;

    public Long getAccount() {
        return account;
    }

    public void setAccount(Long account) {
        this.account = account;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getExternalUid() {
        return externalUid;
    }

    public void setExternalUid(String externalUid) {
        this.externalUid = externalUid;
    }
}
