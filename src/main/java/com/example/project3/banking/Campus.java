package banking;

/**
 * The Campus enum class represents different campus locations.
 * Each campus has a corresponding string name that identifies its location.
 *
 * @author Vishal Saravanan, Yining Chen
 */
public enum Campus {

    /**
     * Represents the campus located in New Brunswick.
     */
    _1("NEW_BRUNSWICK"),

    /**
     * Represents the campus located in Newark.
     */
    _2("NEWARK"),

    /**
     * Represents the campus located in Camden.
     */
    _3("CAMDEN");

    /**
     * Represents the name of the campus as a string.
     * Each value in the Campus enum is associated with a specific name that describes the campus location.
     */
    private final String name;

    /**
     * Constructs a Campus instance with the specified name.
     *
     * @param name the name representing the campus
     */
    Campus(String name) {
        this.name = name;
    }

    /**
     * Converts a string code to its corresponding Campus enum constant.
     *
     * @param code the string code representing the campus
     * @return the Campus enum constant corresponding to the given code
     * @throws IllegalArgumentException if the provided code does not match any Campus enum constant
     */
    public static Campus fromCode(String code) {
        for (Campus campus : Campus.values()) {
            if (campus.name().equals("_" + code)) {
                return campus;
            }
        }
        throw new IllegalArgumentException("Invalid campus code: " + code);
    }

    /**
     * Returns the name representing the campus as a string.
     *
     * @return the name of the campus
     */
    @Override
    public String toString() {
        return name;
    }
}
