package UnitTesting;

import banking.Profile;
import org.junit.Test;
import util.Date;
import static org.junit.Assert.*;

/**
 * The ProfileTest class contains a series of unit tests designed to test the compareTo method
 * in the Profile class.
 *
 * @author Vishal Saravanan, Yining Chen
 */
public class ProfileTest {

    /**
     * Test case #1
     * Tests the case where the profiles have different first names but the first profile's first name is less than
     * the second profile's first name.
     */
    @Test
    public void testProfile_DifferentFirstNameLess() {
        Date date1 = new Date(1, 1, 2000);
        Date date2 = new Date(1, 1, 2000);
        Profile profile1 = new Profile("Alice", "Smith", date1);
        Profile profile2 = new Profile("Bob", "Smith", date2);
        assertEquals(-1, profile1.compareTo(profile2));
    }

    /**
     * Test case #2
     * Tests the case where the profiles have different last names but the first profile's last name is less than
     * the second profile's last name.
     */
    @Test
    public void testProfile_DifferentLastNameLess() {
        Date date1 = new Date(1, 1, 2000);
        Date date2 = new Date(1, 1, 2000);
        Profile profile1 = new Profile("Alice", "Brown", date1);
        Profile profile2 = new Profile("Alice", "Smith", date2);
        assertEquals(-1, profile1.compareTo(profile2));
    }

    /**
     * Test case #3
     * Tests the case where the profiles have the same names but a different date of birth, and the first profile's
     * date of birth comes before the second profile's date of birth.
     */
    @Test
    public void testProfile_DifferentDOBLess() {
        Date date1 = new Date(1, 1, 1990);
        Date date2 = new Date(1, 1, 2000);
        Profile profile1 = new Profile("Alice", "Smith", date1);
        Profile profile2 = new Profile("Alice", "Smith", date2);
        assertEquals(-1, profile1.compareTo(profile2));
    }

    /**
     * Test case #4
     * Tests the case where the profiles have different first names but the first profile's first name is greater than
     * the second profile's first name.
     */
    @Test
    public void testProfile_DifferentFirstNameGreater() {
        Date date1 = new Date(1, 1, 2000);
        Date date2 = new Date(1, 1, 2000);
        Profile profile1 = new Profile("Bob", "Smith", date1);
        Profile profile2 = new Profile("Alice", "Smith", date2);
        assertEquals(1, profile1.compareTo(profile2));
    }

    /**
     * Test case #5
     * Tests the case where the profiles have different last names but the first profile's last name is greater than
     * the second profile's last name.
     */
    @Test
    public void testProfile_DifferentLastNameGreater() {
        Date date1 = new Date(1, 1, 2000);
        Date date2 = new Date(1, 1, 2000);
        Profile profile1 = new Profile("Alice", "Smith", date1);
        Profile profile2 = new Profile("Alice", "Brown", date2);
        assertEquals(1, profile1.compareTo(profile2));
    }

    /**
     * Test case #6
     * Tests the case where the profiles have the same names but a different date of birth, and the first profile's
     * date of birth comes after the second profile's date of birth.
     */
    @Test
    public void testProfile_DifferentDOBGreater() {
        Date date1 = new Date(1, 1, 2000);
        Date date2 = new Date(1, 1, 1990);
        Profile profile1 = new Profile("Alice", "Smith", date1);
        Profile profile2 = new Profile("Alice", "Smith", date2);
        assertEquals(1, profile1.compareTo(profile2));
    }

    /**
     * Test case #7
     * Tests the case where both profiles have the same names and date of births.
     */
    @Test
    public void testProfile_SameProfile() {
        Date date1 = new Date(1, 1, 2000);
        Date date2 = new Date(1, 1, 2000);
        Profile profile1 = new Profile("Alice", "Smith", date1);
        Profile profile2 = new Profile("Alice", "Smith", date2);
        assertEquals(0, profile1.compareTo(profile2));
    }
}
