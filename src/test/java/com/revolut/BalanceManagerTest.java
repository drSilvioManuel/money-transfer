package com.revolut;

import com.revolut.Exception.InvalidOperationException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static junit.framework.TestCase.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BalanceManagerTest {

    private final int from = 5;
    private final int to = 6;

    @Test
    public void checkLazyEvaluationForDeposit() {
        assertEquals(300.0, AccountManager.getBalance(from));
        Response response = BalanceManager.buildDepositResponse(from, 100.0);

        assertEquals(300.0, AccountManager.getBalance(from));

        response.perform();
        assertEquals(400.0, AccountManager.getBalance(from));
    }

    @Test
    public void checkWithdraw() {
        assertEquals(350.0, AccountManager.getBalance(from));

        BalanceManager.buildWithdrawResponse(from, 100.0).perform();

        assertEquals(250.0, AccountManager.getBalance(from));
    }

    @Test
    public void checkTransfer() {
        assertEquals(400.0, AccountManager.getBalance(from));
        assertEquals(400.0, AccountManager.getBalance(to));

        BalanceManager.buildTransferResponse(from, to, 50.0).perform();

        assertEquals(350.0, AccountManager.getBalance(from));
        assertEquals(450.0, AccountManager.getBalance(to));
    }

    @Test(expected = InvalidOperationException.class)
    public void checkFail() {
        BalanceManager.buildTransferResponse(from, to, 500.0).perform();
    }
}
