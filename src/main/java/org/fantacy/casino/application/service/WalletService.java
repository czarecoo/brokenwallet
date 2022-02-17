package org.fantacy.casino.application.service;

import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Public;
import org.fantacy.casino.domain.api.AccountBalanceDTO;
import org.fantacy.casino.domain.api.AccountBalanceQuery;
import org.fantacy.casino.domain.api.CreateAccountCommand;
import org.fantacy.casino.domain.api.CreateAccountDocument;
import org.fantacy.casino.domain.api.CreditAccountCommand;
import org.fantacy.casino.domain.api.DebitAccountCommand;
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
        Account account = new Account();
        account.setPlayerUid(command.getPlayerUid());

        account = accountRepository.saveAndFlush(account);

        CreateAccountDocument doc = new CreateAccountDocument();
        doc.setAccount(account.getId());

        return doc;
    }

    public List<AccountBalanceDTO> accountBalance(AccountBalanceQuery query) {
        List<Account> accounts = accountRepository.findByPlayerUid(query.getPlayerUid());

        return accounts.stream().map(account -> {
            return transactionRepository.findFirstByAccountOrderByIdDesc(account);
        }).filter(Objects::nonNull)
        .map(transaction -> {
            AccountBalanceDTO dto = new AccountBalanceDTO();
            dto.setAccount(transaction.getAccount().getId());
            dto.setBalance(transaction.getBalanceAfter());
            return dto;
        }).collect(Collectors.toList());
    }

    // add money
    public AccountBalanceDTO creditAccount(CreditAccountCommand command) {
        Account account = accountRepository.getById(command.getAccount());
        Transaction lastTransaction = transactionRepository.findFirstByAccountOrderByIdDesc(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setDirection("credit");
        transaction.setExternalUid(command.getExternalUid());
        transaction.setAmount(command.getAmount());

        if (lastTransaction != null) {
            transaction.setBalanceBefore(lastTransaction.getBalanceAfter());
            transaction.setBalanceAfter(lastTransaction.getBalanceAfter() + command.getAmount());
        } else {
            transaction.setBalanceBefore(0D);
            transaction.setBalanceAfter(command.getAmount());
        }

        transactionRepository.saveAndFlush(transaction);

        AccountBalanceDTO dto = new AccountBalanceDTO();
        dto.setAccount(account.getId());
        dto.setBalance(transaction.getBalanceAfter());

        return dto;
    }

    // remove money
    public AccountBalanceDTO debitAccount(DebitAccountCommand command) {
        Account account = accountRepository.getById(command.getAccount());
        Transaction lastTransaction = transactionRepository.findFirstByAccountOrderByIdDesc(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setDirection("debit");
        transaction.setExternalUid(command.getExternalUid());
        transaction.setAmount(command.getAmount());

        if (lastTransaction != null) {
            transaction.setBalanceBefore(lastTransaction.getBalanceAfter());
            transaction.setBalanceAfter(lastTransaction.getBalanceAfter() - command.getAmount());
        } else {
            transaction.setBalanceBefore(0D);
            transaction.setBalanceAfter(command.getAmount());
        }

        transactionRepository.saveAndFlush(transaction);

        AccountBalanceDTO dto = new AccountBalanceDTO();
        dto.setAccount(account.getId());
        dto.setBalance(transaction.getBalanceAfter());

        return dto;
    }

    public List<TransactionDTO> listTransactions(ListTransactionsQuery query) {
        List<Account> accounts = accountRepository.findByPlayerUid(query.getPlayerUid());

        return accounts.stream().flatMap(account -> {
            return transactionRepository.findByAccount(account).stream();
        }).map(transaction -> {
            TransactionDTO dto = new TransactionDTO();
            dto.setAccount(transaction.getAccount().getId());
            dto.setDirection(transaction.getDirection());
            dto.setExternalUid(transaction.getExternalUid());
            dto.setAmount(transaction.getAmount());

            return dto;
        }).collect(Collectors.toList());
    }
}
