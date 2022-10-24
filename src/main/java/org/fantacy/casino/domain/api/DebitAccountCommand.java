package org.fantacy.casino.domain.api;

public record DebitAccountCommand(
    Long account,
    Double amount,
    String externalUid
) {}