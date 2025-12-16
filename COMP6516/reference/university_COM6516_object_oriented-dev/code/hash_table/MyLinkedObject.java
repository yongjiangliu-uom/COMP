package hash_table;
/*
 * Author : Manu Kenchappa Junjanna
 * Email : mkenchappajunjanna1@sheffield.ac.uk
 * Created on Sun Dec 3 2023
 */

/**
 * Represents a linked object in a linked list.
 */
class MyLinkedObject {
    private String word;
    private int count;
    private MyLinkedObject next;

    /**
     * Constructs a new MyLinkedObject with the specified word.
     * The count is initialized to 1 and the next reference is set to null.
     * 
     * @param word the word to be stored in the MyLinkedObject
     */
    public MyLinkedObject(String word) {
        this.word = word;
        this.count = 1;
        this.next = null;
    }

    public void setNext(MyLinkedObject newNext) {
        this.next = newNext;
    }

    public MyLinkedObject getNext() {
        return next;
    }

    public void seCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    /**
     * Sets the word of the MyLinkedObject.
     * If the word is the same as the current word, the count is incremented.
     * If the word is greater than the current word, a new MyLinkedObject is created
     * and set as the next.
     * If the word is less than the current word, the current MyLinkedObject is
     * cloned and the word is updated.
     * 
     * @param w the word to be set
     */
    public int setWord(String w) {
        int result = w.compareTo(word);
        if (result == 0) {
            count++;
            // indicates that the word already exists
            return 0;
        } else if (result > 0) {
            if (next == null) {
                next = new MyLinkedObject(w);
            } else {
                next.setWord(w);
            }
        } else {
            MyLinkedObject newLinkedObject = this.clone();
            word = w;
            next = newLinkedObject;
        }
        // indicates that the new word was added
        return 1;
    }

    public String getWord() {
        return word;
    }

    public MyLinkedObject clone() {
        MyLinkedObject newLinkedObject = new MyLinkedObject(word);
        newLinkedObject.seCount(count);
        newLinkedObject.setNext(next);
        return newLinkedObject;
    }

    @Override
    public String toString() {
        return word + " | " + count + " | --->";
    }
}