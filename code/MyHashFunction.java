// MyHashFunction.java
public abstract class MyHashFunction {
    protected int hashTableSize;

    public MyHashFunction(int size) {
        this.hashTableSize = size;
    }

    public abstract int hash(String s);
}

