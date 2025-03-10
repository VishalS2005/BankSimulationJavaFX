package banking;

import util.Date;

/**
 * Archive will hold Accounts that have been closed
 *
 * @author Vishal Saravanan, Yining Chen
 */
public class Archive {

    /**
     * 'first' represents the head node of the linked list of Account objects
     */
    private AccountNode first;

    /**
     * Creates a new Archive that holds all closed Accounts
     * Linked List data structure representation using AccountNodes are used for the Archive
     */
    public Archive() {
        this.first = null;
    }

    /**
     * Adds an AccountNode object to the front of the linked list instance variable, first.
     *
     * @param account object that will be used to create a AccountNode and will be added to the front of the linked list instance variable, first
     * @param close Date object that represents the Date at which the Account was closed
     */
    public void add(Account account, Date close) {
        AccountNode newNode = new AccountNode(account, close);
        if (first != null) {
            newNode.setNext(first);
        }
        first = newNode;
    }

    /**
     * Traverses through the linked list, first, and prints each account.
     * Every Account Number, Holder, and Balance of the Account are printed from the Archive.
     */
    public void print() {
        System.out.println("\n*List of closed accounts in the archive.");
        AccountNode current = first;
        while (current != null) {
            System.out.println(current);
            current = current.getNext();
        }
        System.out.println("*end of list.\n");
    }
}
