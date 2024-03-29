package org.fantacy.casino.infrastructure.rest;

import lombok.RequiredArgsConstructor;
import org.fantacy.casino.application.service.WalletService;
import org.fantacy.casino.domain.ValidationException;
import org.fantacy.casino.domain.api.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/wallet", consumes = "application/json", produces = "application/json")
public class WalletController {

	private static final String PLAYER_UID_MUST_NOT_BE_NULL_OR_EMPTY = "playerUid must not be null or empty";
	private final WalletService walletService;

	@PostMapping(path = "/create")
	public CreateAccountDocument createAccount(@RequestBody CreateAccountCommand command) {
		if (command.playerUid().isEmpty()) {
			throw new ValidationException(PLAYER_UID_MUST_NOT_BE_NULL_OR_EMPTY);
		}

		return walletService.createAccount(command);
	}

	@PostMapping(path = "/balance")
	public AccountBalanceDocument accountBalance(@RequestBody AccountBalanceQuery query) {
		if (query.playerUid().isEmpty()) {
			throw new ValidationException("playerUid must not be null or empty");
		}

		return walletService.accountBalance(query);
	}

	@PostMapping(path = "/credit")
	public CreditAccountDocument creditAccount(@RequestBody CreditAccountCommand command) {
		return walletService.creditAccount(command);
	}

	@PostMapping(path = "/debit")
	public DebitAccountDocument debitAccount(@RequestBody DebitAccountCommand command) {
		return walletService.debitAccount(command);
	}

	@PostMapping(path = "/list")
	public ListTransactionsDocument listTransactions(@RequestBody ListTransactionsQuery query) {
		if (query.playerUid().isEmpty()) {
			throw new ValidationException("playerUid must not be null or empty");
		}

		return walletService.listTransactions(query);
	}
}