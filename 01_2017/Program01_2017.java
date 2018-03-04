public class Program01_2017 {

    public static void main(String[] args) {
        try {
            int n = Integer.parseInt(args[0]);
            PascalTriangleRow row = new PascalTriangleRow(n);
            for (int i = 1; i < args.length; i++) {
                try {
                    int m = Integer.parseInt(args[i]);
                    System.out.println(m + " - " + row.column(m));
                } catch (NumberFormatException e) {
                    System.err.println(args[i] + " is not an integer");
                } catch (IndexOutOfBoundsException e) {
                    System.err.println(e.getMessage());
                }
            }
        } catch (NumberFormatException e) {
            System.err.println(args[0] + " is not an integer");
        } catch (IndexOutOfBoundsException e) {
            System.err.println(e.getMessage());
        }
    }

}