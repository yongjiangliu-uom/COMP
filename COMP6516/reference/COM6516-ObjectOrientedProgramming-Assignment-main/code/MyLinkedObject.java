
public class MyLinkedObject {
    private String word;
    private int count;
    private MyLinkedObject next;

    public MyLinkedObject(String w){
        this.word = w;  // assign word
        this.count = 1;  // initial values
        this.next = null; // initial values
    };

    /**
     * Add word into linked list
     * @param w word needed to add to linked list
     */
    public void setWord(String w){
        if (w.equals(this.word)){                       // w is equal to word
            this.count ++;
        } else if (this.next == null) {                 // not equal and next not exist
            this.next = new MyLinkedObject(w);
        } else if (w.compareTo(this.next.word) < 0) {   // next exist and alphabetically smaller than next word
            MyLinkedObject next = this.next;
            this.next = new MyLinkedObject(w);
            this.next.next = next;
        }else {                                         // passed on to next object
            this.next.setWord(w);
        }
    };

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
        // TODO: need to fix error
        StringBuilder output = new StringBuilder();
        output.append("MyLinkedObject{");
        output.append("word='").append(word).append('\'');
        output.append(", count=").append(count);
        output.append("}");

        if (next != null) {
            output.append(" -> ");
            output.append(next.toString());
        } else {
            output.append(" -> null");
        }

        return output.toString();
    }


}