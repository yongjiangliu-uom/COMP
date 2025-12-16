// PolynomialHashFunction.java (Task 2 Requirement: Another algorithm)
public class PolynomialHashFunction extends MyHashFunction {
    public PolynomialHashFunction(int size) {
        super(size);
    }

    @Override
    public int hash(String s) {
        if (s == null || s.isEmpty()) return 0;
        int h = 0;
        // Simple polynomial rolling hash
        for (int i = 0; i < s.length(); i++) {
            h = 31 * h + s.charAt(i);
        }
        return Math.abs(h % hashTableSize);
    }
}