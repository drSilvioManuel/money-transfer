package com.revolut;

import com.revolut.Exception.InvalidOperationException;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public class BalanceManager {

    public static Response buildDepositResponse(int id, double money) {
        return () -> AccountManager.deposit(id, money);
    }

    public static Response buildWithdrawResponse(int id, double money) {
        return () -> AccountManager.withdraw(id, money);
    }

    public static Response buildTransferResponse(int from, int to, double money) {
        return () -> AccountManager.transfer(from, to, money);
    }
}
