package com.example.demo;

import com.example.demo.entity.Account;
import com.example.demo.exception.InsufficientFundsException;
import com.example.demo.repository.AccountRepository;
import com.example.demo.service.BankService;
import com.example.demo.service.CollectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DemoRunner implements CommandLineRunner {

    private final BankService bankService;
    private final CollectionService collectionService;
    private final AccountRepository repo;

    @Override
    public void run(String... args) throws Exception {
        Account alice = repo.save(new Account("Alice", BigDecimal.valueOf(1000)));
        Account bob   = repo.save(new Account("Bob",   BigDecimal.valueOf(200)));

        check("transfer",   () -> bankService.transfer(alice.getId(), bob.getId(), BigDecimal.valueOf(9999)));
        check("applyBonus", () -> bankService.applyBonus(alice.getId(), BigDecimal.valueOf(50_000)));
        check("deposit",    () -> bankService.deposit(bob.getId(), BigDecimal.valueOf(300)));
        check("getBalance", () -> bankService.getBalance(alice.getId()));

        System.out.println("===========Задачи на коллекции=================");

        check("removeNegatives", () -> collectionService.removeNegatives(new ArrayList<>(List.of(1, -2, 3, -4))));
        check("addDefaults",     () -> collectionService.addDefaults("a", "b", "c"));
        check("hasDuplicate",    () -> collectionService.hasDuplicate(List.of(1, 2, 2, 3)));
    }

    private void check(String name, Task task) {
        try {
            task.run();
            System.out.println("[OK]   " + name);
        } catch (Exception e) {
            System.out.println("[FAIL] " + name + " -> " + e.getClass().getSimpleName());
        }
    }

    interface Task {
        void run() throws Exception;
    }
}
