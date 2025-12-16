public class MyHashTable {
    private MyLinkedObject[] table;
    private MyHashFunction hashFunction;
    private int size;

    public MyHashTable(int size, MyHashFunction hf) {
        this.size = size;
        this.hashFunction = hf;
        this.table = new MyLinkedObject[size];
    }

    /**
     * Insert a word (or n-gram) into the hash table.
     */
    public void insert(String s) {
        if (s == null || s.isEmpty()) return;

        int index = hashFunction.hash(s);
        
        if (table[index] == null) {
            table[index] = new MyLinkedObject(s);
        } else {
            // Special handling for head of the list if new word is smaller
            if (s.compareTo(table[index].getWord()) < 0) {
                MyLinkedObject newNode = new MyLinkedObject(s);
                newNode.setNext(table[index]);
                table[index] = newNode;
            } else {
                // Use recursive method from MyLinkedObject
                table[index].setWord(s);
            }
        }
    }

    /**
     * Get count of a specific word/n-gram
     */
    public int getCount(String s) {
        int index = hashFunction.hash(s);
        if (table[index] == null) return 0;
        return table[index].getCount(s);
    }

    /**
     * Return the raw table for statistics or iteration
     */
    public MyLinkedObject[] getTable() {
        return table;
    }
}