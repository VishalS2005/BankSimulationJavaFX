package util;

import java.util.Iterator;

/**
 * A generic List implementation that provides dynamic resizing, element addition,
 * removal, and search capabilities. This list dynamically grows its internal
 * storage as elements are added, and supports sequential traversal using iterators.
 *
 * @param <E> the type of elements maintained by this list
 * @author Vishal Saravanan, Yining Chen
 */
public class List<E> implements Iterable<E> {

    /**
     * The growth size used to expand the internal storage of the list when its capacity is exceeded.
     * This constant determines the number of additional slots added to the array during resizing.
     */
    private static final int GROW_SIZE = 4;

    /**
     * The starting size of the AccountDatabase
     */
    private static final int STARTING_SIZE = 0;

    /**
     * The integer -1 is returned if an Account is not found
     */
    private static final int NOT_FOUND = -1;

    /**
     * An array that stores the elements of the list. The size of the array
     * dynamically grows as elements are added to the list and exceeds its
     * current capacity.
     */
    private E[] objects;

    /**
     * Represents the current number of elements in the list.
     */
    private int size;

    /**
     * Constructs a new instance of the List. Initializes the internal storage
     * array with a default initial capacity and sets the initial size of the
     * list.
     */
    public List() {
        this.objects = (E[]) new Object[GROW_SIZE];
        this.size = STARTING_SIZE;
    }

    /**
     * Finds the index of the specified element in the list.
     *
     * @param e the element to search for in the list
     * @return the index of the specified element if it is found, or a predefined constant value NOT_FOUND if it is not found
     */
    private int find(E e) {
        for (int i = 0; i < this.size; i++) {
            if (this.objects[i].equals(e)) {
                return i;
            }
        }
        return NOT_FOUND;
    }

    /**
     * Increases the capacity of the internal storage array to accommodate more elements.
     */
    private void grow() {
        int newLength = this.objects.length + GROW_SIZE; //the new length can hold 4 more accounts than the old length
        E[] newObjects = (E[]) new Object[newLength];
        for (int i = 0; i < this.size; i++) {
            newObjects[i] = this.objects[i];
        }
        this.objects = newObjects;
    }

    /**
     * Determines whether the specified element is present in the list.
     *
     * @param e the element to check for its presence in the list
     * @return true if the element exists in the list, false otherwise
     */
    public boolean contains(E e) {
        return find(e) != NOT_FOUND;
    }

    /**
     * Adds the specified element to the list. If the internal storage array
     * is full, its capacity is increased to accommodate the new element.
     *
     * @param e the element to be added to the list
     */
    public void add(E e) {
        if (this.size == this.objects.length - 1) {
            grow();
        }

        this.objects[this.size] = e;
        this.size++;
    }

    /**
     * Removes the specified element from the list, if it exists. If the element is found,
     * it is replaced by the last element in the list, and the size of the list is decreased.
     *
     * @param e the element to be removed from the list
     */
    public void remove(E e) {
        int index = find(e); //represents index of account
        if (index == -1) {
            return;
        }
        this.objects[index] = this.objects[this.size - 1];
        this.objects[this.size - 1] = null;
        this.size--;
    }

    /**
     * Checks if the list is empty.
     *
     * @return true if the list contains no elements, false otherwise
     */
    public boolean isEmpty() {
        return this.size == STARTING_SIZE;
    }

    /**
     * Returns the number of elements currently in the list.
     *
     * @return the current number of elements in the list
     */
    public int size() {
        return this.size;
    }

    /**
     * Returns an iterator over elements of type {@code E} in this list.
     * The iterator enables traversal of the list in the order elements were added.
     *
     * @return an {@code Iterator<E>} for traversing the list
     */
    @Override
    public Iterator<E> iterator() {
        return new ListIterator();
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index the index of the element to be returned
     * @return the element at the specified position in this list
     * @throws ArrayIndexOutOfBoundsException if the index is out of range
     */
    public E get(int index) {
        return this.objects[index];
    }

    /**
     * Replaces the element at the specified position in this list with the specified element.
     *
     * @param index the position in the list where the specified element is to be stored
     * @param e the element to be stored at the specified position
     * @throws ArrayIndexOutOfBoundsException if the index is out of range
     */
    public void set(int index, E e) {
        this.objects[index] = e;
    }

    /**
     * Returns the index of the specified element in the list, or -1 if the
     * element is not found.
     *
     * @param e the element to search for in the list
     * @return the index of the specified element if it is found, or -1 if it is not found
     */
    public int indexOf(E e) {
        return find(e);
    }

    /**
     * A private inner class that implements the {@code Iterator<E>} interface to allow
     * iterating over the elements of the containing {@code List} class.
     * This iterator traverses the internal array representation of the list in
     * sequential order, starting from the first element.
     */
    private class ListIterator implements Iterator<E> {

        /**
         * Represents the current index position of the iterator while traversing
         * the elements in the list (array).
         */
        int current = 0; //current index when traversing the list (array)

        /**
         * Indicates whether there are more elements to iterate over in the collection.
         *
         * @return true if there are remaining elements to iterate over, false otherwise.
         */
        @Override
        public boolean hasNext() {
            return current < size;
        }

        /**
         * Returns the next element in the iteration and advances the iterator.
         *
         * @return the next element in the iteration
         * @throws java.util.NoSuchElementException if there are no more elements to iterate
         */
        @Override
        public E next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException();
            }
            return objects[current++];
        }
    }
}
