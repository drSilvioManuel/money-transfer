package com.remolut;

import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

public class JsonErrorTest {

    @Test
    public void generalCheck() {
        JsonError je = new JsonError("First", "Second");

        assertEquals("First", je.getType());
        assertEquals("Second", je.getMessage());

        assertEquals(je, new JsonError("First", "Second"));
        assertEquals(je.hashCode(), new JsonError("First", "Second").hashCode());
    }
}
