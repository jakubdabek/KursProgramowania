import java.util.Date;

public class Program {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        System.out.println("Today is " + new Date());

        for (String s : args) {
            try {
                int n = Integer.parseInt(s);
                if (n > 0)
                    System.out.println("The greatest divisor of " + n + " is " + greatestDivisor(n));
                else
                    System.out.println(n + " is not a positive number");
            } catch (NumberFormatException ex) {
                System.out.println(s + " is not an integer");
            }
        }
    }

    public static int greatestDivisor(int n) {
        for (int i = 2; i < Math.sqrt(n) + 1; i++) {
            if (n % i == 0)
                return n / i;
        }
        return 1;
    }

}
