package com.example.lacdpapel;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import android.text.TextUtils;

import com.example.lacdpapel.Activity.Decoration;
import com.example.lacdpapel.Activity.DetailActor;

import java.util.Date;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleUnitTest {



    @Test
    public void testFormatDate() {
        Date date = new Date(0);
        String formattedDate = Decoration.formatDate(date);
        assertEquals("01.01.1970 03:00:00", formattedDate);
    }
    @Test
    public void testFormatAge() {
        assertEquals("Некорректный возраст", DetailActor.formatAge(-1));
        assertEquals("1 год", DetailActor.formatAge(1));
        assertEquals("2 года", DetailActor.formatAge(2));
        assertEquals("4 года", DetailActor.formatAge(4));
        assertEquals("5 лет", DetailActor.formatAge(5));
        assertEquals("11 лет", DetailActor.formatAge(11));
        assertEquals("21 год", DetailActor.formatAge(21));
        assertEquals("22 года", DetailActor.formatAge(22));
        assertEquals("25 лет", DetailActor.formatAge(25));
    }
    @Test
    public void testFormatDate2() {
        String inputDate = "2005-09-21";
        String expectedOutputDate = "21 сентября 2005г";
        String formattedDate = DetailActor.formatDate(inputDate);
        assertEquals(expectedOutputDate, formattedDate);
    }

    @Test
    public void testGenerateOrderNumber() {
        String orderNumber = Decoration.generateOrderNumber();
        assertEquals(6, orderNumber.length());
        assertTrue(orderNumber.matches("[0-9]+"));
    }
    @Test
    public void testFormatDateWithInvalidInput() {
        String invalidInputDate = "invalid-date";
        String formattedDate = DetailActor.formatDate(invalidInputDate);
        assertEquals("", formattedDate);
    }

    @Test
    public void testIsValidCardNumber() {
        assertTrue(Decoration.isValidCardNumber("1234567890123456"));
        assertFalse(Decoration.isValidCardNumber("123456789012345"));
        assertFalse(Decoration.isValidCardNumber("123456789012345A"));
    }

}