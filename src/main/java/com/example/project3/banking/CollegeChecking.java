package banking;

/**
 * CollegeChecking is a specialized type of Checking account tailored for college students.
 * It includes an associated campus to specify the university location and overrides
 * certain behaviors of the Checking account, such as having no monthly fees.
 * This class extends the Checking class.
 *
 * @author Vishal Saravanan, Yining Chen
 */
public class CollegeChecking extends Checking {

    /**
     * Represents the associated campus for the CollegeChecking account.
     * This variable holds the Campus enum value, which indicates the specific university campus.
     */
    private Campus campus;

    /**
     * Constructs a CollegeChecking account with the specified branch, account type,
     * account holder's profile, associated campus, and initial balance.
     * This constructor initializes the campus specific to the CollegeChecking account.
     *
     * @param branch  the branch where the account is created
     * @param type    the type of the account, represented as an AccountType enum
     * @param holder  the profile of the account holder
     * @param campus  the campus associated with the account, represented as a Campus enum
     * @param balance the initial balance of the account
     */
    public CollegeChecking(Branch branch, AccountType type, Profile holder, Campus campus, double balance) {
        super(branch, type, holder, balance);
        this.campus = campus;
    }

    /**
     * Overrides the fee method to calculate the account's monthly fee.
     * For the CollegeChecking account, no fee is applied.
     *
     * @return the monthly fee for the CollegeChecking account, which is always 0.0
     */
    @Override
    public double fee() {
        return NO_FEE;
    }

    /**
     * Converts the CollegeChecking instance to a string representation.
     * The returned string includes the details provided by the superclass's
     * toString method along with the associated campus information.
     *
     * @return a string representation of the CollegeChecking instance,
     * including account details and campus information
     */
    @Override
    public String toString() {
        return super.toString() + " Campus[" + campus + "]";
    }
}

