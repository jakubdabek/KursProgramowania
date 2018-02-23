public class PrimeNumbers {
    public int rowIndex;
    public boolean[] values;

    public PrimeNumbers(int n) {
        if(n < 0)
            throw new IndexOutOfBoundsException(n + " - Wrong index");
        rowIndex = n;
        values = new boolean[n + 1];
        for (int i = 0; i < values.length; i++) values[i] = true;
        values[0] = false;
        values[1] = false;

        for (int i = 2; i < Math.sqrt(n); i++) {
            if (values[i]) {
                for (int j = i * i; j < n; j += i) {
                    values[j] = false;
                }
            }
        }
    }

    public int number(int m) {
        int counter = m;
        for (int i = 0; i < values.length; i++)
        {
            if (values[i] && --counter == -1)
                return i;
        }
        
        throw new IndexOutOfBoundsException(m + " - Number index out of bounds");
    }
}