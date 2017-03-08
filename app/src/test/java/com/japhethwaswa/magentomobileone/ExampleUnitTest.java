package com.japhethwaswa.magentomobileone;

import com.japhethwaswa.magentomobileone.db.JumboContract;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test_path(){
        //arrange
        JumboContract jumboContract = new JumboContract();
        //act
        String content = jumboContract.concatString("com.japhethwaswa.mobilemagento");
        //assert
        assertEquals("content://com.japhethwaswa.mobilemagento",content);
    }
}