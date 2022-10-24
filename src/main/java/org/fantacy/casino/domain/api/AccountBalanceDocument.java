package org.fantacy.casino.domain.api;

import java.util.List;

public record AccountBalanceDocument(
    List<AccountBalanceDTO> balances
) {}