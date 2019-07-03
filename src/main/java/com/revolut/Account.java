package com.revolut;

import com.google.common.util.concurrent.AtomicDouble;
import com.revolut.Exception.InvalidOperationException;
import com.revolut.Exception.NotFoundException;

import javax.annotation.concurrent.ThreadSafe;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

@ThreadSafe
public class Account {

    private static final AtomicInteger counter = new AtomicInteger();
    private static final CopyOnWriteArrayList<Account> mockAccountList = new CopyOnWriteArrayList<>();


    static {
        new Account(100);
        new Account(119);
        new Account(1000);
        new Account(1200);
        new Account(-70);
        new Account(300);
        new Account(400);
    }

    private final int id;
    private AtomicDouble balance;

    private Account(int id, double balance) {
        this.id = id;
        this.balance = new AtomicDouble(balance);

        mockAccountList.add(this);
    }

    Account(double balance) {
        this(counter.getAndIncrement(), balance);
    }

    public int getId() {
        return id;
    }

    public double getBalance() {
        return balance.doubleValue();
    }

    public static List<Account> getMockAccountList() {
        return Collections.unmodifiableList(mockAccountList);
    }

    public static Account getById(int id) {
        if (id < 0 || id >= mockAccountList.size())
            throw new NotFoundException(
                    new JsonError("Not found error", "An account with id: " + id + " is not found"));
        return mockAccountList.get(id);
    }

    public void deposit(double amount) {
        throwIfAmountNegative(amount);

        balance.getAndAdd(amount);
    }

    public void withdraw(double amount) {
        throwIfAmountNegative(amount);

        double currentBalance = balance.doubleValue();
        throwIfBalanceLessThanWithdraw(amount, currentBalance);

        if ( ! balance.compareAndSet(currentBalance, currentBalance - amount))
            throw InvalidOperationException.createOptimisticLock();
    }

    private void throwIfBalanceLessThanWithdraw(double amount, double currentBalance) {
        if (amount > currentBalance) throw InvalidOperationException.createDepositBalanceLessThanWithdraw();
    }

    private void throwIfAmountNegative(double amount) {
        if (amount < 0) throw InvalidOperationException.createDepositNegativeAmount();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return id == account.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
