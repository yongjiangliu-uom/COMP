public class MyLinkedObject {
    private String word;
    private int count;
    private MyLinkedObject next;

    public MyLinkedObject(String w){
        this.word = w;
        this.count = 1;
        this.next = null;
    }

    /**
     * 【关键修改】将递归改为循环 (Iterative)，防止 StackOverflowError
     */
    public void setWord(String w){
        // 1. 检查头节点（当前节点）是否匹配
        if (this.word.equals(w)) {
            this.count++;
            return;
        }

        MyLinkedObject current = this;

        // 使用循环遍历链表，寻找插入位置
        while (current.next != null) {
            // 2. 检查下一个节点是否匹配
            if (current.next.word.equals(w)) {
                current.next.count++;
                return;
            }

            // 3. 按字母顺序插入到 current 和 current.next 之间
            // 如果 w 小于下一个单词，说明找到了插入位置
            if (w.compareTo(current.next.word) < 0) {
                MyLinkedObject newObj = new MyLinkedObject(w);
                newObj.next = current.next;
                current.next = newObj;
                return;
            }

            // 移动到下一个节点
            current = current.next;
        }

        // 4. 如果遍历完还没找到（说明 w 比链表中所有词都大），追加到末尾
        current.next = new MyLinkedObject(w);
    }

    /**
     * 同样建议将查询也改为循环，防止查询深层节点时崩溃
     */
    public boolean isWord(String w) {
        MyLinkedObject current = this;
        while (current != null) {
            if (current.word.equals(w)) {
                return true;
            }
            current = current.next;
        }
        return false;
    }

    /**
     * 同样建议将获取计数也改为循环
     */
    public int getCount(String w) {
        MyLinkedObject current = this;
        while (current != null) {
            if (current.word.equals(w)) {
                return current.count;
            }
            current = current.next;
        }
        return 0;
    }

    // Getters and Setters
    public void setCount(int count) { this.count = count; }
    public void setNext(MyLinkedObject next) { this.next = next; }
    public String getWord() { return word; }
    public int getCount() { return count; }
    public MyLinkedObject getNext() { return next; }

    @Override
    public String toString() {
        return word + ":" + count + (next != null ? " -> " + next.toString() : "");
    }
}