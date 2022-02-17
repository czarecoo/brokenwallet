package org.fantacy.casino.domain.repository;

import org.fantacy.casino.domain.model.Account;
import org.fantacy.casino.domain.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Transaction findFirstByAccountOrderByIdDesc(Account account);
    List<Transaction> findByAccount(Account account);
}
