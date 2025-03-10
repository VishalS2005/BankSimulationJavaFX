package util;

import banking.Account;
import banking.AccountDatabase;

/**
 * This class provides sorting functionality for accounts in an {@code AccountDatabase}.
 * It employs a selection sort algorithm to order the accounts based on a specified key.
 *
 * @author Vishal Saravanan, Yining Chen
 */
public class Sort {

    /**
     * Sorts the accounts in the given {@code AccountDatabase} using a selection sort
     * algorithm based on the specified sorting key.
     *
     * @param list the {@code AccountDatabase} containing the accounts to be sorted
     * @param key  the sorting criterion; valid values include:
     *             'B' for sorting by branch,
     *             'H' for sorting by account holder name,
     *             'T' for sorting by account type.
     *             An {@code IllegalArgumentException} is thrown for invalid keys.
     */
    public static void account(AccountDatabase list, char key) {
        for (int i = 0; i < list.size() - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < list.size(); j++) {
                if (compareAccounts(list.get(j), list.get(minIndex), key) < 0) {
                    minIndex = j;
                }
            }
            if (minIndex != i) {
                Account temp = list.get(i);
                list.set(i, list.get(minIndex));
                list.set(minIndex, temp);
            }
        }
    }

    /**
     * Compares two accounts based on the specified sorting key.
     *
     * @param a1  the first account to be compared
     * @param a2  the second account to be compared
     * @param key the sorting criterion; valid values include:
     *            'B' for comparison by branch,
     *            'H' for comparison by account holder name,
     *            'T' for comparison by account type, with a secondary sort
     *            by account number in case of ties.
     *            An {@code IllegalArgumentException} is thrown for invalid keys.
     * @return a negative integer, zero, or a positive integer as the first account
     *         is less than, equal to, or greater than the second account based on
     *         the specified sorting criterion.
     */
    private static int compareAccounts(Account a1, Account a2, char key) {
        if (key == 'B') {
            return a1.compareByBranch(a2);
        } else if (key == 'H') {
            return a1.compareTo(a2);
        } else if (key == 'T') {
            int typeComparison = a1.compareByAccountType(a2);
            if (typeComparison == 0) {
                return a1.getAccountNumber().compareTo(a2.getAccountNumber());
            }
            return typeComparison;
        } else {
            throw new IllegalArgumentException("Invalid sort key: " + key);
        }
    }
}
