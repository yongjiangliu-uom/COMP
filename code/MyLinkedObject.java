public class MyLinkedObject {
    private String word;
    private int count;
    private MyLinkedObject next;

    /**
     * Constructor (Task 1)
     */
    public MyLinkedObject(String w) {
        this.word = w;
        this.count = 1;
        this.next = null;
    }

    /**
     * Task 1: setWord - Recursive implementation
     * Adds a word or increments count if it exists. Maintain alphabetical order.
     */
    public void setWord(String w) {
        int comparison = w.compareTo(this.word);

        if (comparison == 0) {
            // Word matches, increment count
            this.count++;
        } else if (comparison < 0) {
            // This case should technically be handled by the caller (HashTable) or previous node
            // But if we are at head and w is smaller, logic is handled outside.
            // However, if we are inside the list:
            // This situation usually implies inserting *before* current, which is hard in singly linked list without prev.
            // The standard recursive logic often assumes we look ahead.
            // Let's follow the standard "insert into sorted list" pattern compatible with the PDF description.
            // NOTE: The PDF suggests: "If next object exists, and if w is alphabetically smaller than next... insert between".
            
            // This method is called on 'this'. 
            // If w > this.word, we look at next.
            if (this.next == null) {
                this.next = new MyLinkedObject(w);
            } else {
                if (w.compareTo(this.next.word) < 0) {
                    // w is smaller than next, insert between this and next
                    MyLinkedObject newNode = new MyLinkedObject(w);
                    newNode.next = this.next;
                    this.next = newNode;
                } else {
                    // w is larger or equal to next, recurse
                    this.next.setWord(w);
                }
            }
        } else {
            // w > this.word
            if (this.next == null) {
                this.next = new MyLinkedObject(w);
            } else if (w.compareTo(this.next.word) < 0) {
                // Insert between this and next
                MyLinkedObject newNode = new MyLinkedObject(w);
                newNode.next = this.next;
                this.next = newNode;
            } else {
                // Pass to next
                this.next.setWord(w);
            }
        }
    }

    /**
     * Task 1: isWord - Recursive implementation
     */
    public boolean isWord(String w) {
        if (this.word.equals(w)) {
            return true;
        }
        if (this.next == null) {
            return false;
        }
        return this.next.isWord(w);
    }

    /**
     * Task 1: getCount - Recursive implementation
     */
    public int getCount(String w) {
        if (this.word.equals(w)) {
            return this.count;
        }
        if (this.next == null) {
            return 0;
        }
        return this.next.getCount(w);
    }

    // Getters and Setters
    public String getWord() { return word; }
    public int getCount() { return count; }
    public MyLinkedObject getNext() { return next; }
    public void setNext(MyLinkedObject next) { this.next = next; }
    public void setWordField(String w) { this.word = w; } // Helper if needed
    public void setCount(int c) { this.count = c; }
}