package com.revolut;

import com.revolut.Exception.InvalidOperationException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static junit.framework.TestCase.assertEquals;

public class OperationTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void checkConsistency() {
        Operation operation = new Operation(7895.2);

        assertEquals("money check", 7895.2, operation.money);

        operation = new Operation(7895.2);

        assertEquals("money check", 7895.2, operation.money);

        assertEquals(operation, new Operation(7895.2));
        assertEquals(operation.hashCode(), new Operation(7895.2).hashCode());
    }

    @Test
    public void checkWrongCreation() {
        exception.expect(InvalidOperationException.class);
        new Operation(-1);
    }
}
