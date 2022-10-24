package org.fantacy.casino.infrastructure.rest;

import org.fantacy.casino.application.service.WalletService;
import org.fantacy.casino.domain.ValidationException;
import org.fantacy.casino.domain.api.AccountBalanceDTO;
import org.fantacy.casino.domain.api.AccountBalanceDocument;
import org.fantacy.casino.domain.api.AccountBalanceQuery;
import org.fantacy.casino.domain.api.CreateAccountCommand;
import org.fantacy.casino.domain.api.CreateAccountDocument;
import org.fantacy.casino.domain.api.CreditAccountCommand;
import org.fantacy.casino.domain.api.CreditAccountDocument;
import org.fantacy.casino.domain.api.DebitAccountCommand;
import org.fantacy.casino.domain.api.DebitAccountDocument;
import org.fantacy.casino.domain.api.ListTransactionsDocument;
import org.fantacy.casino.domain.api.ListTransactionsQuery;
import org.fantacy.casino.domain.api.TransactionDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/wallet", consumes = "application/json", produces = "application/json")
public class WalletController {

	private WalletService walletService;

	public WalletController(WalletService walletService) {
		this.walletService = walletService;
	}

	@PostMapping(path = "/create")
	public CreateAccountDocument createAccount(@RequestBody CreateAccountCommand command) {
		if (command.playerUid().isEmpty()) {
			throw new ValidationException("playerUid must not be null or empty");
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