package com.xd.cps2002;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HelloWorld_Test{
    String hw = "HelloWorld!\n";

    // simple test to verify if the greeting function returns Hello World!\n exactly once
    @Test
    public void greetingTest() {
        assertEquals(hw, HelloWorld.greeting());
    }

    /* simple test to verify that the multi_greeting function returns n Hello World!\n statement, and handles
     * erroneous input correctly.
     */
    @Test
    public void multi_greetingTest(){
        // testing the case when 0 is specified for n
        assertEquals("", HelloWorld.multi_greeting(0));

        // testing the case when a valid integer is provided
        String repeated = new String(new char[5]).replace("\0", hw);
        assertEquals(repeated, HelloWorld.multi_greeting(5));
    }
}
