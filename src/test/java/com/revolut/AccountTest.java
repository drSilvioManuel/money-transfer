package com.revolut;

import com.revolut.Exception.InvalidOperationException;
import com.revolut.Exception.NotFoundException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;

public class AccountTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void checkConsistencyGeneralInterface() {

        int initialId = AccountManager.addAccount(1e+10);
        assertEquals("balance check 1", 1e+10, AccountManager.getBalance(initialId));

        int nextId = AccountManager.addAccount(2e+10);
        assertEquals("id check 2", initialId+1, nextId);
        assertEquals("balance check 2", 2e+10, AccountManager.getBalance(nextId));
    }

    @Test
    public void checkConsistencyBalanceInterface() {
        int account = AccountManager.addAccount(2000f);

        AccountManager.deposit(account,1000);

        assertEquals("deposit value check", (double) 2000+1000, AccountManager.getBalance(account));

        AccountManager.withdraw(account, 2999.99f);

        double delta = 0.001;
        assertEquals("withdraw successful", 0.01, AccountManager.getBalance(account), delta);

        exception.expect(InvalidOperationException.class);
        AccountManager.withdraw(account, 0.02f);
        assertEquals("withdraw failed", 0.01, AccountManager.getBalance(account), delta);
    }

    @Test
    public void checkWithdrawNegativeWithNegativeBalance() {
        int account = AccountManager.addAccount(-2000);

        exception.expect(InvalidOperationException.class);
        AccountManager.withdraw(account, -3000);
    }

    @Test
    public void checkWithdrawWithNegativeBalance() {
        int account = AccountManager.addAccount(-2000);

        exception.expect(InvalidOperationException.class);
        AccountManager.withdraw(account, 0.02f);
    }

    @Test
    public void checkDepositWithNegativeValue() {
        int account = AccountManager.addAccount(1000);

        exception.expect(InvalidOperationException.class);
        AccountManager.deposit(account, -0.02f);
    }

    @Test
    public void checkWrongId() {
        exception.expect(NotFoundException.class);
        AccountManager.getById(AccountManager.getMockAccountList().size());
    }

    @Test
    public void checkWrongNegativeId() {
        exception.expect(NotFoundException.class);
        AccountManager.getById(-1);
    }

    @Test
    public void checkHashAndEquality() {
        assertEquals(AccountManager.getById(3), AccountManager.getById(3));
        assertEquals(AccountManager.getById(3).hashCode(), AccountManager.getById(3).hashCode());

        assertNotEquals(AccountManager.getById(AccountManager.addAccount(888)), AccountManager.getById(AccountManager.addAccount(888)));
    }
}

