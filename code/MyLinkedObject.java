public class MyLinkedObject {
    private String word;
    private int count;
    private MyLinkedObject next;

    public MyLinkedObject(String w){
        this.word = w;  // assign word
        this.count = 1;  // initial values
        this.next = null; // initial values
    }

    /**
     * Add word into linked list in alphabetical order or increment count
     * @param w word needed to add
     */
    public void setWord(String w){
        // 1. 检查当前节点是否就是要找的单词 (修复点：之前缺少了这个检查)
        if (this.word.equals(w)) {
            this.count++;
            return;
        }

        // 2. 如果没有下一个节点，直接追加
        if (this.next == null) {
            this.next = new MyLinkedObject(w);
            return;
        }
        
        // 3. 检查下一个节点是否匹配
        if (w.equals(this.next.word)) {
            this.next.count++;
            return;
        }

        // 4. 按字母顺序插入到当前节点和下一个节点之间
        if (w.compareTo(this.next.word) < 0) {
            MyLinkedObject newObj = new MyLinkedObject(w);
            newObj.next = this.next;
            this.next = newObj;
        } else {
            // 5. 递归传递给下一个节点
            this.next.setWord(w);
        }
    }

    /**
     * Task 1: Returns true if the parameter w matches the word field of this object.
     * Recursive implementation.
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
     * Task 1: Returns the value stored in the count field if w matches.
     * Recursive implementation.
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

    public void setCount(int count) {
        this.count = count;
    }

    public void setNext(MyLinkedObject next) {
        this.next = next;
    }

    public String getWord() {
        return word;
    }

    public int getCount() {
        return count;
    }

    public MyLinkedObject getNext() {
        return next;
    }

    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        output.append(word).append(":").append(count);
        
        if (next != null) {
            output.append(" -> ");
            output.append(next.toString());
        }
        return output.toString();
    }
}