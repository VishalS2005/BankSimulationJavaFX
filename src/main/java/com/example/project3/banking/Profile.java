package banking;

import util.Date;

/**
 * The Profile class holds the information regarding a person who holds the bank account,
 * Full name and date of birth are part of their profile.
 *
 * @author Vishal Saravanan, Yining Chen
 */
public class Profile implements Comparable<Profile> {

    /**
     * First name of the account holder.
     */
    private String firstName;

    /**
     * Last name of the account holder.
     */
    private String lastName;

    /**
     * Date of birth of the account holder.
     */
    private Date dateOfBirth;

    /**
     * Creates a Profile object.
     *
     * @param firstName   String representation of the holder's first name
     * @param lastName    String representation of the holder's last name
     * @param dateOfBirth Date object that holds the month, date, and year of holder's birthday
     */
    public Profile(String firstName, String lastName, Date dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Tests 7 different cases for the method, "compareTo".
     * Validates the user input against predefined rules.
     * Test Bed: This method utilizes a test bed as described in the attached write-up.
     *
     * @param args array of command-line arguments passed to the program
     */
    public static void main(String args[]) {
        testProfile_DifferentFirstNameLess();
        testProfile_DifferentLastNameLess();
        testProfile_DifferentDOBLess();
        testProfile_DifferentFirstNameGreater();
        testProfile_DifferentLastNameGreater();
        testProfile_DifferentDOBGreater();
        testProfile_SameProfile();
    }

    /**
     * Test case #1
     * Tests the case where the profiles have different first names but the first profile's first name is less than
     * the second profile's first name.
     */
    private static void testProfile_DifferentFirstNameLess() {
        Date date1 = new Date(1, 1, 2000);                         //1. select first test date
        Date date2 = new Date(1, 1, 2000);                         //2. select second test date
        Profile profile1 = new Profile("Alice", "Smith", date1); //3. select first profile
        Profile profile2 = new Profile("Bob", "Smith", date2);  //4. select second profile
        int expectedOutput = -1;                                                  //5. define expected output
        int actualOutput = profile1.compareTo(profile2);                          //6. test the compareTo method
        System.out.println("**Test case #1: Different first names same last name and DOB " +
                "and profile1 first name < profile2 first name.");
        testProfileResult(profile1, profile2, expectedOutput, actualOutput);//7. compare actual with
        // expected output
    }

    /**
     * Test case #2
     * Tests the case where the profiles have different last names but the first profile's last name is less than
     * the second profile's last name.
     */
    private static void testProfile_DifferentLastNameLess() {
        Date date1 = new Date(1, 1, 2000);                         //1. select test date
        Date date2 = new Date(1, 1, 2000);                         //2. select second test date
        Profile profile1 = new Profile("Alice", "Brown", date1); //3. select first profile
        Profile profile2 = new Profile("Alice", "Smith", date2);//4. select second profile
        int expectedOutput = -1;                                                  //5. define expected output
        int actualOutput = profile1.compareTo(profile2);                          //6. test the compareTo method
        System.out.println("**Test case #2: Different last names same first name and DOB " +
                "and profile1 last name < profile2 last name.");
        testProfileResult(profile1, profile2, expectedOutput, actualOutput);//7. compare actual with
        // expected output
    }

    /**
     * Test case #3
     * Tests the case where the profiles have the same names but a different date of birth, and the first profile's
     * date of birth comes before the second profile's date of birth.
     */
    private static void testProfile_DifferentDOBLess() {
        Date date1 = new Date(1, 1, 1990);                         //1. select first test date
        Date date2 = new Date(1, 1, 2000);                         //2. select second test date
        Profile profile1 = new Profile("Alice", "Smith", date1); //3. select first profile
        Profile profile2 = new Profile("Alice", "Smith", date2);//4. select second profile
        int expectedOutput = -1;                                                  //5. define expected output
        int actualOutput = profile1.compareTo(profile2);                          //6. test the compareTo method
        System.out.println("**Test case #3: Different DOB same first name and last name " +
                "and profile1 DOB < profile2 DOB.");
        testProfileResult(profile1, profile2, expectedOutput, actualOutput);//7. compare actual with
        // expected output
    }

    /**
     * Test case #4
     * Tests the case where the profiles have different first names but the first profile's first name is greater than
     * the second profile's first name.
     */
    private static void testProfile_DifferentFirstNameGreater() {
        Date date1 = new Date(1, 1, 2000);                         //1. select test date
        Date date2 = new Date(1, 1, 2000);                        //2. select second test date
        Profile profile1 = new Profile("Bob", "Smith", date1);   //3. select first profile
        Profile profile2 = new Profile("Alice", "Smith", date2);//4. select second profile
        int expectedOutput = 1;                                                  //5. define expected output
        int actualOutput = profile1.compareTo(profile2);                         //6. test the compareTo method
        System.out.println("**Test case #4: Different first names same last name and DOB " +
                "and profile1 first name > profile2 first name.");
        testProfileResult(profile1, profile2, expectedOutput, actualOutput);//7. compare actual with
        // expected output
    }

    /**
     * Test case #5
     * Tests the case where the profiles have different last names but the first profile's last name is greater than
     * the second profile's last name.
     */
    private static void testProfile_DifferentLastNameGreater() {
        Date date1 = new Date(1, 1, 2000);                         //1. select test date
        Date date2 = new Date(1, 1, 2000);                         //2. select second test date
        Profile profile1 = new Profile("Alice", "Smith", date1); //3. select first profile
        Profile profile2 = new Profile("Alice", "Brown", date2);//4. select second profile
        int expectedOutput = 1;                                                   //5. define expected output
        int actualOutput = profile1.compareTo(profile2);                          //6. test the compareTo method
        System.out.println("**Test case #5: Different last names same first name and DOB " +
                "and profile1 last name > profile2 last name.");
        testProfileResult(profile1, profile2, expectedOutput, actualOutput);//7. compare actual with
        // expected output
    }

    /**
     * Test case #6
     * Tests the case where the profiles have the same names but a different date of birth, and the first profile's
     * date of birth comes after the second profile's date of birth.
     */
    private static void testProfile_DifferentDOBGreater() {
        Date date1 = new Date(1, 1, 2000);                         //1. select first test date
        Date date2 = new Date(1, 1, 1990);                         //2. select second test date
        Profile profile1 = new Profile("Alice", "Smith", date1); //3. select first profile
        Profile profile2 = new Profile("Alice", "Smith", date2);//4. select second profile
        int expectedOutput = 1;                                                   //5. define expected output
        int actualOutput = profile1.compareTo(profile2);                          //6. test the compareTo method
        System.out.println("**Test case #6: Different DOB same first name and last name " +
                "and profile1 DOB > profile2 DOB.");
        testProfileResult(profile1, profile2, expectedOutput, actualOutput);//7. compare actual with
        // expected output
    }

    /**
     * Test case #7
     * Tests the case where both profiles have the same names and date of births.
     */
    private static void testProfile_SameProfile() {
        Date date1 = new Date(1, 1, 2000);                         //1. select first test date
        Date date2 = new Date(1, 1, 2000);                         //2. select second test date
        Profile profile1 = new Profile("Alice", "Smith", date1); //3. select first profile
        Profile profile2 = new Profile("Alice", "Smith", date2);//4. select second profile
        int expectedOutput = 0;                                                   //5. define expected output
        int actualOutput = profile1.compareTo(profile2);                          //6. test the compareTo method
        System.out.println("**Test case #7: Same names and DOB");
        testProfileResult(profile1, profile2, expectedOutput, actualOutput);//7. compare actual with
        // expected output
    }

    /**
     * Helper method to test the compareTo method of the Profile class.
     * Prints the inputs/outputs and FAIL if they don't match and PASS otherwise.
     *
     * @param profile1       the first profile that is being compared
     * @param profile2       the second profile that is being compared
     * @param expectedOutput boolean value we expect from checking if Date is valid
     * @param actualOutput   boolean value as a result of checking if Date is valid
     */
    private static void testProfileResult(Profile profile1, Profile profile2,
                                          int expectedOutput, int actualOutput) {
        System.out.println("Test Input 1: " + profile1);
        System.out.println("Test Input 2: " + profile2);
        System.out.println("Expected output: " + expectedOutput);
        System.out.println("Actual output: " + actualOutput);
        if (expectedOutput != actualOutput) {
            System.out.println(" (FAIL) \n");
        } else {
            System.out.println(" (PASS) \n");
        }
    }

    /**
     * Gets the first name of the holder.
     *
     * @return String representation of the holder's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the last name of the holder
     *
     * @return String representation of the holder's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the birthdate of the holder
     *
     * @return Date object that holds the month, date, and year of holder's birthday
     */
    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Compares two Profile objects
     * Concatenates last name to first name of each Profile object before comparing
     * If names are the same, compares the date of birth next
     *
     * @param other the Profile object to be compared to
     * @return 0 if their names are the same,
     * -1 if first full name/date of birth is less than second
     * 1 if first full name/date of birth is greater than second
     */
    @Override
    public int compareTo(Profile other) {
        String name = this.firstName + this.lastName;
        String otherName = other.firstName + other.lastName;
        int nameComparison = name.compareTo(otherName); //String compareTo() method
        if (nameComparison != 0) {
            return nameComparison < 0 ? -1 : 1;
        }
        return this.dateOfBirth.compareTo(other.dateOfBirth);
    }

    /**
     * Converts Profile to a string that can be printed
     *
     * @return a string that contains the first name, last name, and date of birth of an account holder
     */
    @Override
    public String toString() {
        return this.firstName + " " + this.lastName + " " + this.dateOfBirth;
    }

    /**
     * Compares two Profile objects for equality
     *
     * @param obj other Profile being checked for equality
     * @return true if they are the same object: first name and last name are the same
     * false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Profile) {
            Profile other = (Profile) obj;
            return this.firstName.equalsIgnoreCase(other.firstName)
                    && this.lastName.equalsIgnoreCase(other.lastName)
                    && this.dateOfBirth.equals(other.dateOfBirth);
        }
        return false;
    }
}
