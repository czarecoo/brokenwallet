package org.fantacy.casino.domain.api;

public record AccountBalanceDTO(
    Long account,
    Double balance
) {}