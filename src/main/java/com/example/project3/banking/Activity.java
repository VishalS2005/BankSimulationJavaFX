package banking;

import util.Date;

import java.text.DecimalFormat;

/**
 * Represents a banking activity such as a deposit or withdrawal.
 * An Activity includes a date, location, type, monetary amount, and an indicator
 * of whether the activity occurred through an ATM.
 *
 * @author Vishal Saravanan, Yining Chen
 */
public class Activity implements Comparable<Activity> {

    /**
     * Formatting numbers for easy readability.
     */
    private static final DecimalFormat df = new DecimalFormat("#,##0.00");

    /**
     * Represents the date of the activity.
     */
    private Date date;

    /**
     * Represents the location of the activity.
     */
    private Branch location;

    /**
     * Represents the type of activity performed.
     * The type is denoted by a single character: 'D' for deposit or 'W' for withdrawal.
     */
    private char type;

    /**
     * Represents the monetary value associated with the activity.
     * This variable stores the amount involved in the activity, which can represent either
     * a deposit or a withdrawal, depending on the type of activity.
     */
    private double amount;

    /**
     * Indicates whether the activity was performed at an ATM.
     * This variable is set to true if the activity was conducted through an ATM, as specified in the input text file.
     */
    private boolean atm;

    /**
     * Constructs an Activity object with the specified parameters.
     *
     * @param date     the date of the activity
     * @param location the branch location where the activity took place
     * @param type     the type of activity, represented as a character ('D' for deposit, 'W' for withdrawal)
     * @param amount   the monetary amount involved in the activity
     * @param atm      a boolean indicating whether the activity was performed at an ATM
     */
    public Activity(Date date, Branch location, char type, double amount, boolean atm) {
        this.date = date;
        this.location = location;
        this.type = type;
        this.amount = amount;
        this.atm = atm;
    }

    /**
     * Compares this Activity object with the specified Activity object for order based on the date field.
     *
     * @param o the Activity object to be compared
     * @return a negative integer, zero, or a positive integer as this Activity's date is earlier than,
     * equal to, or later than the specified Activity's date
     */
    @Override
    public int compareTo(Activity o) {
        return date.compareTo(o.date);
    }

    /**
     * Converts the Activity object into a formatted string representation.
     * The format includes the date, branch location, ATM indication (if applicable),
     * transaction type (deposit or withdrawal), and the monetary amount.
     *
     * @return a formatted string representing the Activity object
     */
    @Override
    public String toString() {
        String transactionType = (type == 'D') ? "deposit" : "withdrawal";
        return date + "::" + location + (atm ? "[ATM]" : "") + "::" + transactionType + "::$" + df.format(amount);

    }
}
