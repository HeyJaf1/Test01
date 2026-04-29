package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.exception.InsufficientFundsException;
import com.example.demo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class BankService {

    private final AccountRepository repo;

    @Transactional
    public void transfer(Long fromId, Long toId, BigDecimal amount)
            throws InsufficientFundsException {

        Account from = repo.findById(fromId).orElseThrow();
        Account to   = repo.findById(toId).orElseThrow();

        if (from.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Недостаточно средств");
        }

        from.setBalance(from.getBalance().subtract(amount));
        to.setBalance(to.getBalance().add(amount));
    }

    @Transactional
    public void applyBonus(Long id, BigDecimal bonus) {
        try {
            Account account = repo.findById(id).orElseThrow();
            account.setBalance(account.getBalance().add(bonus));

            if (bonus.compareTo(BigDecimal.valueOf(10_000)) > 0) {
                throw new RuntimeException("Превышен лимит бонуса");
            }
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }

    public void deposit(Long id, BigDecimal amount) {
        doDeposit(id, amount);
    }

    @Transactional
    private void doDeposit(Long id, BigDecimal amount) {
        Account account = repo.findById(id).orElseThrow();
        account.setBalance(account.getBalance().add(amount));
        repo.save(account);
    }

    @Transactional(readOnly = true)
    public BigDecimal getBalance(Long id) {
        Account account = repo.findById(id).orElseThrow();
        account.setBalance(account.getBalance().add(BigDecimal.ONE));
        return account.getBalance();
    }
}
