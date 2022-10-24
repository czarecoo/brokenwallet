package org.fantacy.casino.domain.api;

public record TransactionDTO(
    Long account,
    String direction,
    String externalUid,
    Double amount
) {}