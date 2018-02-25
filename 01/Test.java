public class Test {

    public static void main(String[] args) {
        try {
            int n = Integer.parseInt(args[0]);
            PrimeNumbers numbers = new PrimeNumbers(n);
            for (int i = 1; i < args.length; i++) {
                try {
                    int m = Integer.parseInt(args[i]);
                    System.out.println(m + " - " + numbers.number(m));
                } catch (NumberFormatException e) {
                    System.out.println(args[i] + " is not an integer");
                } catch (IndexOutOfBoundsException e) {
                    System.out.println(e.getMessage());
                }
            }
        } catch (NumberFormatException e) {
            System.out.println(args[0] + " is not an integer");
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
    }

}