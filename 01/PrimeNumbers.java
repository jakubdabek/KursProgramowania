import java.util.ArrayList;
import java.util.List;

public class PrimeNumbers {

    public int rowIndex;
    public List<Integer> values;

    public PrimeNumbers(int n) {
        if(n < 0)
            throw new IndexOutOfBoundsException(n + " - Wrong index");

        rowIndex = n;
        boolean[] sieve = new boolean[n + 1];
        for (int i = 0; i < sieve.length; i++) sieve[i] = true;
        sieve[0] = false;
        sieve[1] = false;

        for (int i = 2; i <= Math.sqrt(n + 1); i++) {
            if (sieve[i]) {
                for (int j = i * i; j <= n; j += i) {
                    sieve[j] = false;
                }
            }
        }

        values = new ArrayList<>();
        for(int i = 0; i < sieve.length; i++)
        {
            if(sieve[i])
                values.add(i);
        }
    }

    public int number(int m) {
        if(m > values.size())
            throw new IndexOutOfBoundsException(m + " - Number index out of bounds");
        return values.get(m);
    }

}