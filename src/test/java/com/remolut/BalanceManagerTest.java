package com.remolut;

import com.remolut.Exception.InvalidOperationException;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static junit.framework.TestCase.assertEquals;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BalanceManagerTest {

    private final BalanceManager balanceManager = new BalanceManager();
    private final Account from = Account.getById(5);
    private final Account to = Account.getById(6);

    @Test
    public void checkLazyEvaluationForDeposit() {
        assertEquals(300.0, from.getBalance());
        Response response = balanceManager.buildResponse(new RequestMessage.Update(from.getId(), new Operation(Operation.TYPE.DEPOSIT.getId(), 100.0)));

        assertEquals(300.0, from.getBalance());

        response.perform();
        assertEquals(400.0, from.getBalance());
    }

    @Test
    public void checkWithdraw() {
        assertEquals(350.0, from.getBalance());

        balanceManager.buildResponse(new RequestMessage.Update(from.getId(), new Operation(Operation.TYPE.WITHDRAW.getId(), 100.0))).perform();

        assertEquals(250.0, from.getBalance());
    }

    @Test
    public void checkTransfer() {
        assertEquals(400.0, from.getBalance());
        assertEquals(400.0, to.getBalance());

        balanceManager.buildResponse(new RequestMessage.Transfer(from.getId(), to.getId(), new Operation(Operation.TYPE.TRANSFER.getId(), 50.0))).perform();

        assertEquals(350.0, from.getBalance());
        assertEquals(450.0, to.getBalance());
    }

    @Test(expected = InvalidOperationException.class)
    public void checkFail() {
        balanceManager.buildResponse(new RequestMessage.Transfer(from.getId(), to.getId(), new Operation(Operation.TYPE.TRANSFER.getId(), 500.0))).perform();
    }

    @Test(expected = InvalidOperationException.class)
    public void checkFail2() {
        balanceManager.buildResponse(new RequestMessage.Transfer(from.getId(), to.getId(), new Operation(Operation.TYPE.NONE.getId(), 500.0))).perform();
    }
}
