public class Test {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Too few arguments");
            return;
        }

        if (args[0].compareToIgnoreCase("A") != 0 && args[0].compareToIgnoreCase("R") != 0) {
            System.out.println(
                    "Wrong arguments\n" +
                    "Put mode as the first argument: " +
                    "'R' for roman to arabic, 'A' for arabic to roman");
            return;
        }

        boolean roman2arabic = args[0].compareToIgnoreCase("R") == 0;

        for (int i = 1; i < args.length; i++) {
            try {
                if (roman2arabic) {
                    int value = RomanArabic.Roman2Arabic(args[i]);
                    System.out.println("Value of " + args[i] + " is " + value);
                } else {
                    String str = RomanArabic.Arabic2Roman(Integer.parseInt(args[i]));
                    System.out.println(args[i] + " in roman is " + str);
                }
            } catch (RomanArabicException e) {
                System.out.println(e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println(args[i] + " is not an integer");
            }
        }
    }

}