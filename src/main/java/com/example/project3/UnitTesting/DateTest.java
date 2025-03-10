package UnitTesting;

import org.junit.Test;
import util.Date;
import static org.junit.Assert.*;

/**
 * The DateTest class provides a suite of unit tests to validate the functionality
 * of the Date class.
 *
 * @author Vishal Saravanan, Yining Chen
 */
public class DateTest {

    /**
     * Test case #1
     * Tests the case where the number of days in February is 29 in a non-leap year.
     */
    @Test
    public void testDate_FebNonLeapYear() {
        Date date = new Date(2, 29, 2023);
        assertFalse(date.isValid());
    }

    /**
     * Test case #2
     * Tests the case where the amount of days in August does not fall within the valid range of [1,31].
     */
    @Test
    public void testDate_DayOutOfRangePositive() {
        Date date = new Date(8, 32, 2021);
        assertFalse(date.isValid());
    }

    /**
     * Test case #3
     * Tests the case where the month is outside valid range of [1,12].
     */
    @Test
    public void testDate_MonthOutOfRange() {
        Date date = new Date(23, 29, 1900);
        assertFalse(date.isValid());
    }

    /**
     * Test case #4
     * Tests the case where the amount of days in September does not fall within the valid range of [1,31].
     */
    @Test
    public void testDate_DayOutOfRangeNegative() {
        Date date = new Date(9, -30, 2000);
        assertFalse(date.isValid());
    }

    /**
     * Test case #5
     * Tests the case where the amount of days in September does not fall within the valid range of [1,31].
     */
    @Test
    public void testDate_RandomDate() {
        Date date = new Date(8, 23, 2008);
        assertTrue(date.isValid());
    }

    /**
     * Test case #6
     * Tests the case where the number of days in February during a leap year is 29.
     */
    @Test
    public void testDate_FebLeapYear() {
        Date date = new Date(2, 29, 2024);
        assertTrue(date.isValid());
    }
}
