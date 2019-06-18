package com.revolut;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class RequestMessageTest {

    @Test
    public void checkUpdateMessage(){
        RequestMessage.Update message = new RequestMessage.Update(777, new Operation(1, 77));

        assertEquals(777, message.getId());
        assertEquals(message.getOperation(), new Operation(1, 77));
        assertEquals(message, new RequestMessage.Update(777, new Operation(1, 77)));
        assertEquals(message.hashCode(), new RequestMessage.Update(777, new Operation(1, 77)).hashCode());
    }

    @Test
    public void checkTransferMessage(){
        RequestMessage.Transfer message = new RequestMessage.Transfer(777, 888, new Operation(1, 77));

        assertEquals(777, message.getId());
        assertEquals(888, message.getIdTo());
        assertEquals(message.getOperation(), new Operation(1, 77));
        assertEquals(message, new RequestMessage.Transfer(777, 888, new Operation(1, 77)));
        assertEquals(message.hashCode(), new RequestMessage.Transfer(777, 888, new Operation(1, 77)).hashCode());
    }
}
