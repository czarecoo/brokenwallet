package org.fantacy.casino.domain.api;

public record CreditAccountCommand(
    Long account,
    Double amount,
    String externalUid
) {}