package banking;

/**
 * The Branch enum class holds the information regarding all the different types of Branch locations.
 * Each Branch object has a 5-digit ZIP code, 3-digit branch code, and the county the branch is located in.
 *
 * @author Vishal Saravanan, Yining Chen
 */
public enum Branch {

    /**
     * Representation of the Edison Branch - 08817, 100, Middlesex.
     */
    EDISON("08817", "100", "Middlesex"),

    /**
     * Representation of the Bridgewater Branch - 08807, 200, Somerset.
     */
    BRIDGEWATER("08807", "200", "Somerset"),

    /**
     * Representation of the Princeton Branch - 08542, 300, Mercer.
     */
    PRINCETON("08542", "300", "Mercer"),

    /**
     * Representation of the Piscataway Branch - 08854, 400, Middlesex.
     */
    PISCATAWAY("08854", "400", "Middlesex"),

    /**
     * Representation of the Warren Branch - 07057, 500, Somerset.
     */
    WARREN("07057", "500", "Somerset");

    /**
     * 5-digit number that represents the ZIP code.
     */
    private final String zip;

    /**
     * 3-digit number that represents the branch code.
     */
    private final String branchCode;

    /**
     * String that represents the county the branch is located in.
     */
    private final String county;

    /**
     * Creates a Branch object.
     *
     * @param zip        5-digit ZIP code that represents location
     * @param branchCode 3-digit representation of the Branch
     * @param county     String representation of the county
     */
    Branch(String zip, String branchCode, String county) {
        this.zip = zip;
        this.branchCode = branchCode;
        this.county = county;
    }

    /**
     * Returns a 3-digit String representation of the Branch.
     *
     * @return 3-digit String representation of the Branch
     */
    public String getBranchCode() {
        return this.branchCode;
    }

    /**
     * Returns a String representation of the name of the Branch.
     *
     * @return String representation of the name of the Branch
     */
    public String getCounty() {
        return this.county;
    }

    /**
     * Converts Branch object to a string that can be printed.
     *
     * @return the 3-digit branch code as a String
     */
    @Override
    public String toString() {
        return name();
    }
}
