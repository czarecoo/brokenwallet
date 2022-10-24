package org.fantacy.casino.domain.api;

import java.util.List;

public record ListTransactionsDocument(
    List<TransactionDTO> balances
) {}
