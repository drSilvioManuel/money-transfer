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
        Account account = new Account(1e+10);

        int initialId = account.getId();
        assertEquals("balance check 1", 1e+10, account.getBalance());

        account = new Account(2e+10);
        assertEquals("id check 2", initialId+1, account.getId());
        assertEquals("balance check 2", 2e+10, account.getBalance());
        assertSame("index check", account, Account.getById(account.getId()));
    }

    @Test
    public void checkConsistencyBalanceInterface() {
        Account account = new Account(2000f);

        account.deposit(1000);

        assertEquals("deposit value check", (double) 2000+1000, account.getBalance());

        account.withdraw(2999.99f);

        double delta = 0.001;
        assertEquals("withdraw successful", 0.01, account.getBalance(), delta);

        exception.expect(InvalidOperationException.class);
        account.withdraw(0.02f);
        assertEquals("withdraw failed", 0.01, account.getBalance(), delta);
    }

    @Test
    public void checkWithdrawNegativeWithNegativeBalance() {
        Account account = new Account(-2000);

        exception.expect(InvalidOperationException.class);
        account.withdraw(-3000);
    }

    @Test
    public void checkWithdrawWithNegativeBalance() {
        Account account = new Account(-2000);

        exception.expect(InvalidOperationException.class);
        account.withdraw(0.02f);
    }

    @Test
    public void checkDepositWithNegativeValue() {
        Account account = new Account(1000);

        exception.expect(InvalidOperationException.class);
        account.deposit(-0.02f);
    }

    @Test
    public void checkWrongId() {
        exception.expect(NotFoundException.class);
        Account.getById(Account.getMockAccountList().size());
    }

    @Test
    public void checkWrongNegativeId() {
        exception.expect(NotFoundException.class);
        Account.getById(-1);
    }

    @Test
    public void checkHashAndEquality() {
        assertEquals(Account.getById(3), Account.getById(3));
        assertEquals(Account.getById(3).hashCode(), Account.getById(3).hashCode());

        assertNotEquals(new Account(888), new Account(888));
        assertNotEquals(new Account(888).hashCode(), new Account(888).hashCode());
    }
}

