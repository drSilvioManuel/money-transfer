package com.revolut;

import com.revolut.Exception.InvalidOperationException;
import com.revolut.Exception.NotFoundException;

import javax.annotation.concurrent.Immutable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.StampedLock;
import java.util.stream.Collectors;

@ThreadSafe
public class AccountManager {

    private static final AtomicInteger counter = new AtomicInteger();
    private static final CopyOnWriteArrayList<Account> mockAccountList = new CopyOnWriteArrayList<>();
    private static final StampedLock LOCK = new StampedLock();

    static {
        new Account(100);
        new Account(119);
        new Account(1000);
        new Account(1200);
        new Account(-70);
        new Account(300);
        new Account(400);
    }

    private AccountManager() {}

    public static int addAccount(double balance) {
        return new Account(balance).getId();
    }

    public static double getBalance(int id) {
        long stamp = LOCK.tryOptimisticRead();
        double balance = AccountManager.getById(id).getAccount().getBalance();
        if (LOCK.validate(stamp)) {
            return balance;
        } else {
            stamp = LOCK.readLock();
            try {
                return AccountManager.getById(id).getAccount().getBalance();
            } finally {
                LOCK.unlockRead(stamp);
            }
        }
    }

    public static void deposit(int id, double amount) {
        long stamp = LOCK.writeLock();
        try {
            AccountManager.getById(id).getAccount().deposit(amount);
        } finally {
            LOCK.unlockWrite(stamp);
        }
    }

    public static void withdraw(int id, double amount) {
        long stamp = LOCK.writeLock();
        try {
            AccountManager.getById(id).getAccount().withdraw(amount);
        } finally {
            LOCK.unlockWrite(stamp);
        }
    }

    public static void transfer(int idFrom, int idTo, double amount) {
        ImmutableAccount from = AccountManager.getById(idFrom);
        ImmutableAccount to = AccountManager.getById(idTo);

        long stamp = LOCK.writeLock();
        try {
            from.getAccount().withdraw(amount);
            to.getAccount().deposit(amount);

        } catch (InvalidOperationException e) {
            
            from.resetBalance();
            to.resetBalance();

            throw e;
        } finally {
            LOCK.unlockWrite(stamp);
        }
    }

    public static List<ImmutableAccount> getMockAccountList() {
        return Collections.unmodifiableList(
                mockAccountList
                        .stream()
                        .map(ImmutableAccount::new)
                        .collect(Collectors.toList()));
    }

    public static ImmutableAccount getById(int id) {
        if (id < 0 || id >= mockAccountList.size())
            throw new NotFoundException(
                    new JsonError("Not found error", "An account with id: " + id + " is not found"));
        return new ImmutableAccount(mockAccountList.get(id));
    }

    @Immutable
    public static class ImmutableAccount {
        private final int id;
        private final double balance;
        private final Account account;

        private ImmutableAccount(Account account) {
            this.id = account.getId();
            this.balance = account.getBalance();
            this.account = account;
        }

        public int getId() {
            return id;
        }

        public double getBalance() {
            return balance;
        }

        public void resetBalance() {
            account.setBalance(balance);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ImmutableAccount that = (ImmutableAccount) o;
            return getId() == that.getId();
        }

        @Override
        public int hashCode() {
            return Objects.hash(getId());
        }

        private Account getAccount() {
            return account;
        }
    }

    public static class Account {
        private final int id;
        private double balance;

        private Account(int id, double balance) {
            this.id = id;
            this.balance = balance;

            mockAccountList.add(this);
        }

        private Account(double balance) {
            this(counter.getAndIncrement(), balance);
        }

        public int getId() {
            return id;
        }

        private double getBalance() {
            return balance;
        }

        private void setBalance(double amount) {
            balance = amount;
        }

        private void deposit(double amount) {
            throwIfAmountNegative(amount);

            balance += amount;
        }

        private void withdraw(double amount) {
            throwIfAmountNegative(amount);

            double currentBalance = balance;
            throwIfBalanceLessThanWithdraw(amount, currentBalance);

            balance -= amount;
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
}
