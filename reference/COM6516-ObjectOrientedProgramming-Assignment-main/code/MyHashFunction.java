
/**
    Supper Class for multiple hash algorithms
 */
public abstract class MyHashFunction {
    public int hashTableSize;

    protected MyHashFunction(int hashTableSize) {
        this.hashTableSize = hashTableSize;
    }

    public int hash(String wordSequence) {
        return -1; // 或其他默认值
    }

    public void setHashTableSize(int hashTableSize){
        this.hashTableSize = hashTableSize;
    }

}
