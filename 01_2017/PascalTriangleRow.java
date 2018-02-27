public class PascalTriangleRow {

    public int rowIndex;
    public int[] values;

    public PascalTriangleRow(int n) throws IndexOutOfBoundsException {
        if (n < 0)
            throw new IndexOutOfBoundsException(n + " - Wrong row index");
        rowIndex = n;
        values = new int[n + 1];
        values[0] = 1;
        values[values.length - 1] = 1;
        for (int i = 1; i < n / 2 + 1; i++) {
            values[i] = values[i - 1] * n / i;
            values[values.length - i] = values[i];
            n--;
        }
    }

    public int column(int m) {
        if (m < values.length && m >= 0)
            return values[m];
        else
            throw new IndexOutOfBoundsException(m + " - Column index out of bounds");
    }

}