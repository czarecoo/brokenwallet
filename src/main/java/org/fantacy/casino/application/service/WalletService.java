package org.fantacy.casino.application.service;

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
import org.fantacy.casino.domain.model.Account;
import org.fantacy.casino.domain.model.Transaction;
import org.fantacy.casino.domain.repository.AccountRepository;
import org.fantacy.casino.domain.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class WalletService{

    private AccountRepository accountRepository;
    private TransactionRepository transactionRepository;

    public WalletService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    public CreateAccountDocument createAccount(CreateAccountCommand command) {
        Account account = new Account(command.playerUid());

        account = accountRepository.saveAndFlush(account);

        return new CreateAccountDocument(account.getId());
    }

    public AccountBalanceDocument accountBalance(AccountBalanceQuery query) {
        List<Account> accounts = accountRepository.findByPlayerUid(query.playerUid());

        return new AccountBalanceDocument(accounts.stream().map(account -> {
            return transactionRepository.findFirstByAccountOrderByIdDesc(account);
        }).filter(Objects::nonNull)
        .map(transaction -> {
            return new AccountBalanceDTO(
                    transaction.getAccount().getId(),
                    transaction.getBalanceAfter());
        }).collect(Collectors.toList()));
    }

    // add money
    public CreditAccountDocument creditAccount(CreditAccountCommand command) {
        Account account = accountRepository.getById(command.account());
        Transaction lastTransaction = transactionRepository.findFirstByAccountOrderByIdDesc(account);

        double balanceAfter = 0D;
        double balanceBefore = 0D;

        if (lastTransaction != null) {
            balanceBefore = lastTransaction.getBalanceAfter();
            balanceAfter = lastTransaction.getBalanceAfter() + command.amount();
        } else {
            balanceAfter = command.amount();
        }

        Transaction transaction = new Transaction(
                account,
                "credit",
                command.externalUid(),
                command.amount(),
                balanceBefore,
                balanceAfter);

        transactionRepository.saveAndFlush(transaction);

        return new CreditAccountDocument(new AccountBalanceDTO(
            account.getId(),
            transaction.getBalanceAfter()));
    }

    // remove money
    public DebitAccountDocument debitAccount(DebitAccountCommand command) {
        Account account = accountRepository.getById(command.account());
        Transaction lastTransaction = transactionRepository.findFirstByAccountOrderByIdDesc(account);

        double balanceAfter = 0D;
        double balanceBefore = 0D;

        if (lastTransaction != null) {
            balanceBefore = lastTransaction.getBalanceAfter();
            balanceAfter = lastTransaction.getBalanceAfter() - command.amount();
        } else {
            balanceAfter = command.amount();
        }

        Transaction transaction = new Transaction(
                account,
                "debit",
                command.externalUid(),
                command.amount(),
                balanceBefore,
                balanceAfter
        );

        transactionRepository.saveAndFlush(transaction);

        return new DebitAccountDocument(new AccountBalanceDTO(
            account.getId(),
            transaction.getBalanceAfter()));
    }

    public ListTransactionsDocument listTransactions(ListTransactionsQuery query) {
        List<Account> accounts = accountRepository.findByPlayerUid(query.playerUid());

        return new ListTransactionsDocument(accounts.stream().flatMap(account -> {
            return transactionRepository.findByAccount(account).stream();
        }).map(transaction -> {
            return new TransactionDTO(
                transaction.getAccount().getId(),
                transaction.getDirection(),
                transaction.getExternalUid(),
                transaction.getAmount());
        }).collect(Collectors.toList()));
    }
}
